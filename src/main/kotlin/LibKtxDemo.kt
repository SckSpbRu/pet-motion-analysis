import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.VertexAttributes.Usage.*
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.utils.UBJsonReader
import ktx.app.KtxApplicationAdapter

fun main(args: Array<String>) {
  val config = Lwjgl3ApplicationConfiguration()
  config.setWindowSizeLimits(800, 600, 800, 600)
  Lwjgl3Application(My3DTest(), config)
}

class My3DTest : KtxApplicationAdapter {
  lateinit var cam: PerspectiveCamera

  lateinit var modelBatch: ModelBatch
  lateinit var environment: Environment
  lateinit var cameraInputController: CameraInputController
  val models: MutableList<Model> = mutableListOf()
  val instances: MutableList<ModelInstance> = mutableListOf()

  override fun create() {
    modelBatch = ModelBatch()

    cam = PerspectiveCamera(67f,
        Gdx.graphics.getWidth().toFloat(),
        Gdx.graphics.getHeight().toFloat())
    cam.translate(50f, -100f, 50f)
    cam.lookAt(0f, -0f, -0f)
    cam.near = 1f;
    cam.far = 800f
    cam.update()

    val box = ModelBuilder().createBox(5f, 5f, 5f,
        Material(ColorAttribute.createDiffuse(Color.GREEN)),
        (Position or Normal).toLong())
    models.add(box)
    instances.add(ModelInstance(box))


    val cyl = ModelBuilder().createCylinder(5f, 20f, 5f, 10,
        Material(ColorAttribute.createDiffuse(Color.GREEN)),
        (Position or Normal).toLong())
    models += cyl
    instances += ModelInstance(cyl)

    val g3dModelLoader = G3dModelLoader(UBJsonReader())
    val untitled = g3dModelLoader.loadModel(Gdx.files.getFileHandle("untitled.g3db", Files.FileType.Internal))
    models += untitled
    instances += ModelInstance(untitled)

    environment = Environment()
    environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
    environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))

    cameraInputController = CameraInputController(cam)
    Gdx.input.inputProcessor = cameraInputController
  }

  override fun render() {
    cameraInputController.update()

    Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

    modelBatch.begin(cam)
    println(cam.direction)
    modelBatch.render(instances, environment)
    modelBatch.end()
  }

  override fun dispose() {
    modelBatch.dispose()
    models.forEach { it.dispose() }
  }
}
