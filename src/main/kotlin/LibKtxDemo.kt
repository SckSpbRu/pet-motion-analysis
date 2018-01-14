import com.badlogic.gdx.Gdx
import ktx.app.KtxApplicationAdapter
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.utils.Logger.DEBUG

fun main(args: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()
    config.disableAudio(true)
    config.setWindowSizeLimits(800,600,800,600)
    Lwjgl3Application(MyApplicationListener(), config)
    Gdx.app.setLogLevel(DEBUG);
}

class MyApplicationListener : KtxApplicationAdapter {
    lateinit var cam: Camera
    lateinit var model: Model
    lateinit var instance: ModelInstance
    lateinit var modelBatch: ModelBatch

    override fun create() {
        cam = OrthographicCamera(10f, 10f);

        model = ModelBuilder().createBox(5f, 5f, 5f,
                Material(ColorAttribute.createAmbient(Color.CYAN)),
                VertexAttributes.Usage.Position.toLong())
        instance = ModelInstance(model)

        modelBatch = ModelBatch()
    }

    override fun render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)


        modelBatch.begin(cam)
        modelBatch.render(instance)
        modelBatch.end()
    }

    override fun dispose() {
        modelBatch.dispose()
        model.dispose()
    }
}