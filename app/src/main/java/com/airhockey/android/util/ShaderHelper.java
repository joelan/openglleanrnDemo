package com.airhockey.android.util;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Created by JC001 on 2017/1/11.
 */

public class ShaderHelper {

    private static final String TAG = "ShaderHelper";

    /**编译顶点着色器
     * @param shaderCode 着色代码
     * @return
     */
    public static int compileVertexShader(String shaderCode) {

        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 编译片段着色器
     * @param shaderCode  着色代码
     * @return
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }
    private static int compileShader(int type, String shaderCode) {

        //根据类型创建着色器
        final int shaderObjectId = glCreateShader(type);
        if (shaderObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new shader.");
            }
            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode);
        //编译Shader程序
        glCompileShader(shaderObjectId);
        final int[] compileStatus = new int[1];

        //获取Shader的编译情况
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
        if (LoggerConfig.ON) {
              // Print the shader info log to the Android log output.
            Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:"
                    + glGetShaderInfoLog(shaderObjectId));
        }
        //输出Shader状态
        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object.
            glDeleteShader(shaderObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG, "Compilation of shader failed.");
            }
            return 0;
        }


        return  shaderObjectId;

    }


    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new program");
            }
            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        if (LoggerConfig.ON) {
// Print the program info log to the Android log output.
            Log.v(TAG, "Results of linking program:\n"
                    + glGetProgramInfoLog(programObjectId));
        }
        if (linkStatus[0] == 0) {
// If it failed, delete the program object.
            glDeleteProgram(programObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG, "Linking of program failed.");
            }
            return 0;
        }
       return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog:" + glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }

}