package com.example.bodyvaldez.paint_with_sockets;

        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.graphics.drawable.Drawable;
        import android.os.AsyncTask;
        import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.Toast;

        import com.github.nkzawa.emitter.Emitter;
        import com.github.nkzawa.socketio.client.IO;
        import com.github.nkzawa.socketio.client.Socket;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.net.URISyntaxException;

public class CanvasView extends View {
    public static float x,y;
    Paint paint = new Paint();
    // 1 to move at point on screen and 2 to move the finger on the screen
    public static int auxiliar=0;
    //Properties of the paint
    int lineStroke = 1;
    //path
    Path path = new Path();
    //Socket
    Socket mSocket;
    public CanvasView(Context context) {
        super(context);
    }
    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Connect the socket to the server and port
        try {
            mSocket = IO.socket("http://192.168.1.68:3000/");
            System.out.println("Otro socket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("Conected");
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
        mSocket.on("path", onNewMessage);
        mSocket.connect();
    }
    //Open new listener that will read the new data emmited of the server.
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject  data = (JSONObject) args[0];
            NewThread hilo = new NewThread();
            hilo.execute(data);
        }
    };

    public void onDraw(Canvas canvas){
        //Set the paint style
        paintStyle(this.paint);

        if (auxiliar == 1) {
          path.moveTo(x,y);
        }
        if (auxiliar == 2) {
            path.lineTo(x,y);
        }

        //paint each path on the canvas
        canvas.drawPath(path, paint);

    }

    public void paintStyle(Paint paint){
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineStroke);
    }

/**
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
    }
*/

    public boolean onTouchEvent(MotionEvent e){
        x = e.getX();
        y = e.getY();

        //if the user only touch the screen the path just moves his position
        if(e.getAction() ==e.ACTION_DOWN){
            auxiliar = 1;
        }
        //if the user move his finger on the screen it paints on the path
        if(e.getAction() == e.ACTION_MOVE){
            auxiliar = 2;
        }
        //call the draw method
        invalidate();
        //send the data of the path
        mSocket.emit("path", setJson(x,y,auxiliar));
        return true;
    }

    //set the json that is going to send.
    public JSONObject setJson(float x,float y,int auxiliar){
        JSONObject dataOfPath = new JSONObject();
        try {
            dataOfPath.put("x", x);
            dataOfPath.put("y", y);
            dataOfPath.put("auxiliar", auxiliar);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return  dataOfPath;
    }

    //Read the data that are recieved of the server.
    public class NewThread extends AsyncTask<JSONObject,Void,Void> {
        float x2,y2;
        int a2;
        @Override
        protected Void doInBackground(JSONObject... path) {
         // System.out.println("lol");
            try {
                x2 = (float) path[0].getDouble("x");
                y2 = (float) path[0].getDouble("y");
                a2 = path[0].getInt("auxiliar");

                System.out.println("x: "+x+" y"+y);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // add the message to view
            return null;
        }
        //Draw the data as paint
        @Override
        protected void onPostExecute(Void f) {
            CanvasView.x = x2;
            CanvasView.y = y2;
            CanvasView.auxiliar=a2;
            invalidate();
        }
    }

}
