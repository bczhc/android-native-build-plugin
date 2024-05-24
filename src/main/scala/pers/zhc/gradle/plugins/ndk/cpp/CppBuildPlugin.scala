package pers.zhc.gradle.plugins.ndk.cpp

import org.gradle.api.provider.Property
import org.gradle.api.{Plugin, Project, Task}
import pers.zhc.android.`def`.{AndroidTarget, BuildType}
import pers.zhc.gradle.plugins.ndk.NdkUtils.JMap
import pers.zhc.gradle.plugins.ndk.{NdkBaseExtension, NdkUtils}
import pers.zhc.gradle.plugins.ndk.cpp.CppBuildPlugin.{CLEAN_TASK_NAME, COMPILE_TASK_NAME, Configurations, CppBuildPluginExtension}
import pers.zhc.gradle.plugins.util.FileUtils

import java.io.File
import scala.jdk.CollectionConverters.MapHasAsScala

class CppBuildPlugin extends Plugin[Project] {
  override def apply(project: Project): Unit = {
    val extension =
      project.getExtensions.create("cppBuild", classOf[CppBuildPluginExtension])

    project.task(
      COMPILE_TASK_NAME,
      { task: Task =>
        task.doLast({ _: Task =>
          buildTask(extToConfigs(extension))
        })
        ()
      }
    )

    project.task(
      CLEAN_TASK_NAME,
      { task: Task =>
        task.doLast({ _: Task =>
          cleanTask(extToConfigs(extension))
        })
        ()
      }
    )
  }

  private def buildTask(configs: Configurations): Unit = {
    new BuildRunner(configs).run()
  }

  private def cleanTask(configs: Configurations): Unit = {
    FileUtils.requireDelete(new File(configs.srcDir, "build"))
  }

  private def extToConfigs(
                            extensions: CppBuildPluginExtension
                          ): Configurations = {
    def unwrap[T] = NdkUtils.unwrapProperty[T] _

    new Configurations {
      override val jniLibDir: File = new File(
        unwrap(extensions.getOutputDir, "outputDir")
      )
      override val srcDir: File = new File(
        unwrap(extensions.getSrcDir, "srcDir")
      )
      override val ndkDir: File = new File(
        unwrap(extensions.getNdkDir, "ndkDir")
      )
      override val cmakeBinDir: File = new File(
        unwrap(extensions.getCmakeBinDir, "getCmakeBinDir")
      )
      override val targets: List[AndroidTarget] =
        NdkUtils.propertyToTargets(extensions.getTargets)
      override val buildType: BuildType =
        BuildType.from(unwrap(extensions.getBuildType, "buildType"))
      override val cmakeDefs: Option[Map[String, Map[String, String]]] =
        Option(extensions.getCmakeDefs.getOrNull())
          .map({ x =>

            val scala =
              x.asInstanceOf[JMap[String, JMap[String, String]]].asScala
            scala.view.mapValues(_.asScala.toMap).toMap
          })
    }
  }
}

object CppBuildPlugin {
  val COMPILE_TASK_NAME = "compileCpp"
  val CLEAN_TASK_NAME = "cleanCpp"

  trait CppBuildPluginExtension extends NdkBaseExtension {
    def getCmakeBinDir: Property[String]

    def getCmakeDefs: Property[Any]
  }

  abstract class Configurations {
    val jniLibDir: File
    val srcDir: File
    val ndkDir: File
    val cmakeBinDir: File
    val targets: List[AndroidTarget]
    val buildType: BuildType
    val cmakeDefs: Option[Map[String, Map[String, String]]]
  }
}
