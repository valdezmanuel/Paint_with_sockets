package com.example.bodyvaldez.paint_with_sockets;


    import android.app.Activity;
    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Color;
    import android.graphics.Paint;
    import android.graphics.Path;
    import android.os.AsyncTask;
    import android.support.v7.app.ActionBarActivity;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.SeekBar;
    import android.widget.TextView;
    import android.widget.Toast;


    import com.github.nkzawa.emitter.Emitter;
    import com.github.nkzawa.socketio.client.IO;
    import com.github.nkzawa.socketio.client.Socket;
    import org.json.JSONException;
    import org.json.JSONObject;
    import java.net.URISyntaxException;

    public class MainActivity extends Activity {
        CanvasView canvasView;
        //TextView chat;
        //EditText mensaje;
        //Button enviar;
        SeekBar sb;

        //Socket
        //private Socket mSocket;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //Socket connect Test
            /**
            try {
                mSocket = IO.socket("http://192.168.1.68:3000/");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                   // mSocket.emit("chat message", "Usuario conectado");
                    //mSocket.disconnect();
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
            mSocket.on("chat message",onNewMessage);
            mSocket.connect();

            if( mSocket.connected()){
                Toast.makeText(getApplication(),"Conectado",Toast.LENGTH_SHORT).show();
            }
            */

            canvasView = (CanvasView) findViewById(R.id.canvasView);
            sb = (SeekBar) findViewById(R.id.seekBar);

            //OnProgressChanged event of the seekbar
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChanged = 0;

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChanged = progress;
                    //Change line stroke
                    canvasView.lineStroke = progress;
                }
                public void onStartTrackingTouch(SeekBar seekBar) {}

                public void onStopTrackingTouch(SeekBar seekBar) {
                    Toast.makeText(getApplication(), "Line Stroke: " + progressChanged,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
/** Test with socket.io
        private Emitter.Listener onNewMessage = new Emitter.Listener() {

            @Override
            public void call(final Object... args) {
                JSONObject data = (JSONObject) args[0];
                NewThread thread = new NewThread();
                thread.execute(data);
            }
        };

        //Read the new Messages in a new Thread
        public class NewThread extends AsyncTask<JSONObject,Void,Void>{
            Double username;
            String message;
            @Override
            protected Void doInBackground(JSONObject... params) {
                JSONObject data = params[0];
                System.out.println(params[0].toString());


                try {
                   username = data.getDouble("id");
                    message = data.getString("msg");
                } catch (JSONException e) {
                    System.out.println(e);
                    System.out.println("Error");
                }
                // add the message to view
                return null;
            }

            @Override
            protected void onPostExecute(Void f) {
                addMessage(this.username,this.message);
            }
        }

        public void addMessage(Double id,String msg){
            chat.setText(chat.getText().toString()+"\n"+Double.toString(id)+" : "+msg);
        }
            */
    }



