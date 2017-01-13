package lrq.com.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.airhockey.android.util.LoggerConfig;
import com.airhockey.android.util.ShaderHelper;
import com.airhockey.android.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by JC001 on 2017/1/11.
 */

public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    Context context;
    private int program;

   // private static final String U_COLOR = "u_Color";
    //private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;


    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;


    public AirHockeyRenderer(Context context) {

        this.context=context;


      /*  float[] tableVerticesWithTriangles = {
// Triangle 1
                0f, 0f,
                9f, 14f,
                0f, 14f,
// Triangle 2
                0f, 0f,
                9f, 0f,
                9f, 14f,
// Line 1
                0f, 7f,
                9f, 7f,
// Mallets
                4.5f, 2f,
                4.5f, 12f
        };*/
/*        float[] tableVerticesWithTriangles = {
// Triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,
// Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
// Line 1
                -0.5f, 0f,
                0.5f, 0f,
// Mallets
                0f, -0.25f,
                0f, 0.25f
        };*/


        float[] tableVerticesWithTriangles = {
// Order of coordinates: X, Y, R, G, B
// Triangle Fan
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
// Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,
// Mallets
                0f, -0.25f, 0f, 0f, 1f,
                0f, 0.25f, 1f, 0f, 0f
        };


        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);





    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //对应的参数是R,G,B,A颜色参数值
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //获取opengl代码字符串
        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_fragment_shader);


        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        //链接程序
        program=ShaderHelper.linkProgram(vertexShader,fragmentShader);
        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }
        glUseProgram(program);

       // uColorLocation = glGetUniformLocation(program, U_COLOR);

        //关联颜色
        aColorLocation = glGetAttribLocation(program, A_COLOR);



        //关联位置
        aPositionLocation = glGetAttribLocation(program, A_POSITION);



    /*   glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, 0, vertexData);*/




        //颜色
        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        //Enable数据才有效
        glEnableVertexAttribArray(aColorLocation);


        //线条
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        //Enable数据才有效
        glEnableVertexAttribArray(aPositionLocation);



    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GL_COLOR_BUFFER_BIT);

       // glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
       // glDrawArrays(GL_TRIANGLES, 0, 6);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

   //     glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

       // glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);


       // glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }

}
