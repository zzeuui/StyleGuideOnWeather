package com.example.v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class Fragment3 extends Fragment {

    Context context;

    ViewGroup rootView;

    private TextView tv_location;
    private TextView tv_temperature;
    private ImageView imageview;
    private TextView textView;

    private static final int FROM_ALBUM = 1;    // onActivityResult 식별자
    private static final int FROM_CAMERA = 2;   //
    private static final int REQUEST_IMAGE_CAPTURE = 672;

    private String imageFilePath;
    private Bitmap bmp;
    private String text;
    private int temperature;

    private int current_location_text_size = 18;
    private int temperatures_text_size = 55;
    private int check_result_text_size = 45;

    private String model_path ="season_model.tflite";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        imageview = (ImageView) rootView.findViewById(R.id.frag3_iv);
        tv_location = (TextView)rootView.findViewById(R.id.frag3_tv_location);
        tv_temperature = (TextView)rootView.findViewById(R.id.frag3_tv_temperature);
        textView = (TextView)rootView.findViewById(R.id.result) ;

        set_texts_style();

        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.frag3_bt_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTakePhotoIntent(); //카메라에 접
            }


        });

        view.findViewById(R.id.frag3_bt_album).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK); //내부 저장소에 접근하는 intent
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, FROM_ALBUM); //앨범에서 이미지를 얻어온다. onActivityResult로 이동.
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        float imgData[][][][] = null; //input: [batch][width][height][channel]

        //IMAGE VIEW로보여
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bmp = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();

            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);


            } else {
                exifDegree = 0;
            }



            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            bmp = rotate(imageBitmap, exifDegree);
            imageview.setImageBitmap(bmp);


        } else if (requestCode == FROM_ALBUM  && data != null && data.getData() != null){
            try{
                InputStream in = context.getContentResolver().openInputStream(data.getData()); //앨범으로부터 이미지를 얻어옴.
                bmp = BitmapFactory.decodeStream(in); //bitmap으로 변환
                imageview.setImageBitmap(bmp);

                //camera image uri to bitmap

                in.close(); //inputstream close\

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgData = new float[1][bmp.getWidth()][bmp.getHeight()][3]; //얻어온 이미지의 width height로 input shape 결정

        //차례대로 picxxel 정보를 4차원 형태의 imgData에 담
        for (int x = 0; x < bmp.getWidth(); x++) {
            {
                for(int y = 0; y <bmp.getHeight(); y++){
                    int pixel = bmp.getPixel(x, y);

                    imgData[0][x][y][0] = Color.red(pixel);
                    imgData[0][x][y][1] = Color.green(pixel);
                    imgData[0][x][y][2] = Color.blue(pixel);
                }
            }
        }

        Interpreter tf_lite = getTfliteInterpreter(model_path); //model 준

        float[][] output = new float[1][4]; //output 결과를 담을 변수 [1][number of classes]
        tf_lite.run(imgData, output); // model run


        // 텍스트뷰 5개, classes에 대한 score가 담김.
        int[] id_array = {R.id.frag3_result_0, R.id.frag3_result_1, R.id.frag3_result_2, R.id.frag3_result_3};

        //각각의 score set view
        for(int i = 0; i < id_array.length; i++) {
            TextView tv = rootView.findViewById(id_array[i]);
            tv.setText(String.format("%.5f", output[0][i]));    // [0] : 2차원 배열의 첫 번째
        }

        ScoreCloth(temperature, id_array);

    }

    //model 부르는 함수
    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile((Activity) getContext(), modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //model 부르는 함수
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    //camera
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("captureCamera Error", ex.toString());
            }

            if (photoFile != null) {
/*                photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
                imageUri = photoUri;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);*/

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                // bmp = (Bitmap) takePictureIntent.getExtras().get("data");
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_" + text + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    //회전
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    //회전
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void ScoreCloth(int c, int id_array[]) {

        int maxi = ReturnIndex(id_array);

        switch (maxi) {

            //겨울
            case 3:
                if (c <= 8) {
                    //Log.d(this.getContext(), (String) textView.getText());
                    textView.setText("BEST!");

                } else {
                    textView.setText("BAD!");
                }
                //가을
            case 0:
                if (c <= 16 && c >= 9) {
                    textView.setText("BEST!");
                } else {
                    textView.setText("BAD!");
                }

                //여름
            case 2:
                if (c >= 23) {
                    textView.setText("BEST!");
                } else {
                    textView.setText("BAD!!");
                }
                //봄
            case 1:
                if (c <= 22 && c >= 17) {
                    textView.setText("BEST!");
                } else {
                    textView.setText("BAD!");
                }


        }
    }

    private int ReturnIndex(int[] id_array){
        int maxi = 0;
        double max_n = id_array[0];
        for(int i = 1; i < 4; i ++){
            if(max_n < id_array[i]){
                maxi = i;
                max_n = id_array[i];
            }
        }
        return maxi;
    }

    public void setText(String s, int i) throws IOException {
        switch (i){
            case 1:
                tv_location.setText(s);
                break;
            case 2:
                tv_temperature.setText(s);
                temperature = Integer.parseInt(s);
                imageview.setImageResource(R.drawable.hanger);
                break;
        }
    }

    private void set_texts_style(){
        tv_location.setTextSize(current_location_text_size);
        tv_location.setTextColor(Color.BLACK);

        tv_temperature.setTextSize(temperatures_text_size);
        tv_temperature.setTextColor(Color.BLACK);

        textView.setTextSize(check_result_text_size);
        textView.setTextColor(Color.BLACK);
    }
}
