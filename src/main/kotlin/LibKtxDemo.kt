import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes.Usage.*
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.UBJsonReader
import ktx.app.KtxApplicationAdapter
import ktx.math.vec3

fun main(args: Array<String>) {
  val config = Lwjgl3ApplicationConfiguration()
  config.setWindowSizeLimits(1600, 1200, 1600, 1200)
  Lwjgl3Application(My3DTest(), config)
}

class My3DTest : KtxApplicationAdapter {
  lateinit var cam: PerspectiveCamera

  lateinit var modelBatch: ModelBatch
  lateinit var environment: Environment
  lateinit var cameraInputController: CameraInputController
  lateinit var nikishInstance: ModelInstance
  val models: MutableList<Model> = mutableListOf()
  val instances: MutableList<ModelInstance> = mutableListOf()

  override fun create() {
    modelBatch = ModelBatch()

    cam = PerspectiveCamera(67f,
        Gdx.graphics.getWidth().toFloat(),
        Gdx.graphics.getHeight().toFloat())
    cam.translate(10f, 10f, 10f)
    cam.lookAt(0f, 0f, 0f)
    cam.near = 1f;
    cam.far = 100f
    cam.update()

    val texture = Texture(Gdx.files.internal("nikish.png"))
    val material = Material(TextureAttribute.createDiffuse(texture))
    val attr = (Position or Normal or TextureCoordinates).toLong()

    val nikish = ModelBuilder()
        .createSphere(3f, 3f, 3f, 100, 100,
            material, attr)
    models += nikish
    nikishInstance = ModelInstance(nikish)
    instances += nikishInstance

    val g3dModelLoader = G3dModelLoader(UBJsonReader())
    val room = g3dModelLoader
        .loadModel(
            Gdx.files.getFileHandle("room_prototype.g3db", Files.FileType.Internal))
    models += room
    instances += ModelInstance(room)

    environment = Environment()
    environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
    environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0.7f, 0.2f))
    environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, 0.8f, -0.2f))

    cameraInputController = CameraInputController(cam)
    Gdx.input.inputProcessor = cameraInputController
  }

  var angle1 = .0f
  val rotAngle1 = vec3(x = Math.random().toFloat(),
      y = Math.random().toFloat(),
      z = Math.random().toFloat())

  override fun render() {
    cameraInputController.update()

    Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

    nikishInstance.transform = Matrix4(vec3(0f, 0f, 0f),
        Quaternion(rotAngle1, angle1++),
        Vector3(1.0f, 1.0f, 1.0f))

    modelBatch.begin(cam)
    modelBatch.render(instances, environment)
    modelBatch.end()
  }

  override fun dispose() {
    modelBatch.dispose()
    models.forEach { it.dispose() }
  }
}
