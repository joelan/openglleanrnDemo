package lrq.com.opengldemo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
// Assign our renderer.
        glSurfaceView.setRenderer(new AirHockeyRenderer(this));

        setContentView(glSurfaceView);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(glSurfaceView!=null)
        glSurfaceView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(glSurfaceView!=null)
        glSurfaceView.onPause();

    }
}
