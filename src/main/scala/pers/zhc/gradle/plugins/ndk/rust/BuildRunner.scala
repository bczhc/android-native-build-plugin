package pers.zhc.gradle.plugins.ndk.rust

import pers.zhc.android.`def`.{AndroidTarget, BuildType}
import pers.zhc.gradle.plugins.ndk.rust.BuildRunner.BuildOptions
import pers.zhc.gradle.plugins.ndk.rust.RustBuildPlugin.Environments
import pers.zhc.gradle.plugins.util.ProcessUtils

import java.io.File
import scala.jdk.CollectionConverters.SeqHasAsJava

/** @author
  *   bczhc
  */
class BuildRunner(toolchain: Toolchain, options: BuildOptions) {
  def run(): Int = {

    val runtime = Runtime.getRuntime
    val rustTarget = options.target.abi.toRustTriple
    var command = List(
      "cargo",
      "build",
      "--target",
      rustTarget,
      s"-j${runtime.availableProcessors()}"
    )
    if (options.buildType == BuildType.RELEASE) {
      command = command :+ "--release"
    }

    val pb = new ProcessBuilder(command.asJava)
    val env = pb.environment()
    env.put("TARGET_CC", toolchain.linker.getPath)
    env.put("TARGET_AR", toolchain.ar.getPath)
    options.env.foreach(_.foreach({ i => env.put(i._1, i._2) }))

    val targetEnvName = rustTarget.replace('-', '_').toUpperCase
    env.put(
      s"CARGO_TARGET_${targetEnvName}_LINKER",
      toolchain.linker.getPath
    )

    pb.directory(options.rustProjectDir)
    val progress = pb.start()

    ProcessUtils.executeWithOutput(progress)
  }
}

object BuildRunner {
  case class BuildOptions(
      target: AndroidTarget,
      buildType: BuildType,
      env: Option[Environments],
      rustProjectDir: File
  )
}
