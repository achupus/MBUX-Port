package com.rndash.mbheadunit.doom

import android.graphics.Bitmap
import android.opengl.GLES20.*
import android.opengl.GLUtils
import com.rndash.mbheadunit.doom.renderer.ColourMap
import com.rndash.mbheadunit.doom.wad.Patch
import com.rndash.mbheadunit.doom.wad.WadFile
import java.nio.ByteBuffer
import java.nio.IntBuffer

object Renderer {
    const val vertexShaderDOOM = """
        uniform mat4 u_MVPMatrix;
        attribute vec4 a_Position;
        attribute vec2 a_texcoords;
        varying vec2 v_texcoords;
        
        void main() {
            v_texcoords = a_texcoords;
            gl_Position = u_MVPMatrix * a_Position;
        }
    """

    const val fragmentShaderDOOM = """
        precision mediump float; 

        uniform sampler2D u_sampler;
        varying vec2 v_texcoords;
        
        void main() {
            gl_FragColor = texture2D(u_sampler, v_texcoords);
        }
    """

    const val vertexShaderBS = """
        uniform mat4 u_MVPMatrix;
        attribute vec4 vPosition;
        attribute vec4 vColour;
        varying vec4 _vColour;
        void main() {
            _vColour = vColour;
            gl_Position = u_MVPMatrix * vPosition;
        }
    """

    const val fragmentShaderBS = """
        precision mediump float;
        varying vec4 _vColour;
        vec4 ambientlighting = vec4(1, 1, 1, 1);
        void main() {
            gl_FragColor = _vColour * ambientlighting;
        }
    """


    fun loadShader(str: String, type: Int): Int {
        var shaderHandle = glCreateShader(type)
        if (shaderHandle != 0) {
            glShaderSource(shaderHandle, str)
            glCompileShader(shaderHandle)

            val compileStatus = IntArray(1)
            glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] == 0) {
                System.err.println(glGetShaderInfoLog(shaderHandle))
                glDeleteShader(shaderHandle)
                shaderHandle = 0
            }
        }
        return shaderHandle
    }

    fun createProgramDOOM(): Int {
        var handle = glCreateProgram()
        if (handle != 0) {
            loadShader(vertexShaderDOOM, GL_VERTEX_SHADER).let {
                if (it != 0) { glAttachShader(handle, it) } else {
                    throw Exception("Error attaching vertex shader")
                }
            }
            loadShader(fragmentShaderDOOM, GL_FRAGMENT_SHADER).let {
                if (it != 0) { glAttachShader(handle, it) } else {
                    throw Exception("Error attaching fragment shader")
                }
            }

            glBindAttribLocation(handle, 0, "a_Position")
            glBindAttribLocation(handle, 1, "a_Color")
            glLinkProgram(handle)

            val linkStatus = IntArray(1)
            glGetProgramiv(handle, GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                System.err.println(glGetProgramInfoLog(handle))
                glDeleteProgram(handle)
                handle = 0
            }
        }
        return handle
    }

    fun createProgramBS(): Int {
        var handle = glCreateProgram()
        if (handle != 0) {
            loadShader(vertexShaderBS, GL_VERTEX_SHADER).let {
                if (it != 0) { glAttachShader(handle, it) } else {
                    throw Exception("Error attaching vertex shader")
                }
            }
            loadShader(fragmentShaderBS, GL_FRAGMENT_SHADER).let {
                if (it != 0) { glAttachShader(handle, it) } else {
                    throw Exception("Error attaching fragment shader")
                }
            }

            glBindAttribLocation(handle, 0, "a_Position")
            glBindAttribLocation(handle, 1, "a_Color")
            glLinkProgram(handle)

            val linkStatus = IntArray(1)
            glGetProgramiv(handle, GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                System.err.println(glGetProgramInfoLog(handle))
                glDeleteProgram(handle)
                handle = 0
            }
        }
        return handle
    }

    @ExperimentalUnsignedTypes
    fun loadTexture(name: String, w: WadFile, p: Array<ColourMap>): Int {
        val texHandle = IntArray(1)
        glGenTextures(1, texHandle, 0)
        if (texHandle[0] != 0) {
            w.cacheTexture(name)?.let {
                it.cacheTexture(w, p) // Cache the RGB values from colour palette
                val bitmap = Bitmap.createBitmap(it.header.width.toInt(), it.header.height.toInt(), Bitmap.Config.ARGB_8888)
                it.rgba.position(0)
                bitmap.copyPixelsFromBuffer(it.rgba)
                glActiveTexture(GL_TEXTURE0)
                glBindTexture(GL_TEXTURE_2D, texHandle[0])
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
                GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
                bitmap.recycle()
            }
        } else {
            System.err.println("Error caching texture $name")
        }
        return texHandle[0]
    }

    @ExperimentalUnsignedTypes
    fun loadFlat(name: String, w: WadFile, p: Array<ColourMap>): Int {
        val texHandle = IntArray(1)
        glGenTextures(1, texHandle, 0)
        if (texHandle[0] != 0) {
            w.readFlat(name).let {
                println("loading flat $name")
                val bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
                val rgba = IntBuffer.allocate(64*64*4)
                for (i in 0 until it.capacity()) {
                    rgba.put(p[0].getRgb(it[i].toInt() and 0xFF))
                }
                rgba.position(0)
                bitmap.copyPixelsFromBuffer(rgba)
                glActiveTexture(GL_TEXTURE0)
                glBindTexture(GL_TEXTURE_2D, texHandle[0])
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
                GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
                bitmap.recycle()
            }
        } else {
            System.err.println("Error caching texture $name")
        }
        return texHandle[0]
    }

    @ExperimentalUnsignedTypes
    fun loadPatch(patch: Patch, map: ColourMap): Int {
        val pHandle = IntArray(1)
        glGenTextures(1, pHandle, 0)
        if (pHandle[0] != 0) {
            val bitmap = Bitmap.createBitmap(patch.width, patch.height, Bitmap.Config.ARGB_8888)
            val rgba = IntBuffer.allocate(patch.width*patch.height)
            for (y in 0 until patch.height) {
                for (x in 0 until patch.width) {
                    val pixel = map.getRgb(patch.pixels[(y*patch.width)+x].toInt() and 0xFF)
                    if (x in 0..patch.width && y in 0..patch.height) {
                        rgba.put((y * patch.width) + x, pixel)
                    }
                }
            }
            rgba.position(0)
            bitmap.copyPixelsFromBuffer(rgba)
            glActiveTexture(GL_TEXTURE0)
            glBindTexture(GL_TEXTURE_2D, pHandle[0])
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
        } else {
            System.err.println("Error caching patch $patch")
        }
        return pHandle[0]
    }
}