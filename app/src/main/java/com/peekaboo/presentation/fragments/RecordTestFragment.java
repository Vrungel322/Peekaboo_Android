package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.domain.AudioRecorder;
import com.peekaboo.domain.Record;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by sebastian on 05.08.16.
 */
public class RecordTestFragment extends Fragment {

    private OkHttpClient client;
    private boolean isRecording;
    private Record record;
    private AudioRecorder audioRecorder;
    private Button button;
    private TextView messages;
    private Spinner langSpinner;
    private Spinner voiceSpinner;
    private Spinner thresholdSpinner;
    private Spinner sampleRateSpinner;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();
        record = new Record("test1");
        audioRecorder = new AudioRecorder(record);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.record_fragment, container, false);
        langSpinner = (Spinner) inflate.findViewById(R.id.lang_spinner);
        voiceSpinner = (Spinner) inflate.findViewById(R.id.voice_spinner);
        thresholdSpinner = (Spinner) inflate.findViewById(R.id.threshold_spinner);
        messages = (TextView) inflate.findViewById(R.id.messages);
        sampleRateSpinner = (Spinner) inflate.findViewById(R.id.sample_rate_spinner);

        button = (Button) inflate.findViewById(R.id.record_button);
        button.setOnClickListener(v -> {
            if (!isRecording) {
                Integer sampleRate = Integer.valueOf((String) sampleRateSpinner.getSelectedItem());
//                record.setSampleRate(sampleRate);
                audioRecorder.startRecording().subscribe();
                button.setText("Stop Record");
                messages.setText("");
            } else {
                audioRecorder.stopRecording().subscribe(record1 -> {
                    sendFile(record1.getFilename());
                });
                button.setText("Start Record");
                messages.setText("Sending...");
            }
            isRecording = !isRecording;
        });
        langSpinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                new String[]{"en-UK", "en-US"}));
        voiceSpinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                new String[]{"BroadbandModel", "NarrowbandModel"}));
        sampleRateSpinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                new String[]{"11025", "16000", "22050", "32000"}));
        thresholdSpinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                new String[]{"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9"}));
        return inflate;
    }

    private void sendFile(String fileName) {
//        String fileName = Environment.getExternalStorageDirectory().toString() + File.separator + "eric.wav";
        File file = new File(fileName);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("task_file", "file",
                        RequestBody.create(MediaType.parse("audio/flac"), file))
                .build();
        Request request = new Request.Builder()
                .url("http://text-to-speech-java-oleksiiromanko.mybluemix.net/UploadServlet")
//                .url("http://10.0.1.5:8080/UploadServlet")
                .addHeader("language", (String) langSpinner.getSelectedItem())
                .addHeader("voice", (String) voiceSpinner.getSelectedItem())
                .addHeader("evidence threshold", (String) thresholdSpinner.getSelectedItem())
                .addHeader("level", (String) sampleRateSpinner.getSelectedItem())
                .post(requestBody)
                .build();
//
//
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", String.valueOf(e));
                messages.post(() -> messages.setText(String.valueOf(e)));
            }

            //
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("response", String.valueOf(response));

                String string = response.body().string();
                Log.e("response", String.valueOf(Thread.currentThread()));
                messages.post(() -> messages.setText(Html.fromHtml(string)));
            }
        });
    }

}
