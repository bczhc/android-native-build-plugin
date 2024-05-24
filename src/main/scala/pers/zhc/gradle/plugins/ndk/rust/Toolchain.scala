package pers.zhc.gradle.plugins.ndk.rust

import pers.zhc.android.`def`.AndroidAbi

import java.io.File

/** @author
  *   bczhc
  */
class Toolchain(
    val ndkPath: File,
    val androidApi: Int,
    val androidAbi: AndroidAbi
) {
  private val binDir = ToolchainUtils.getToolchainBinDir(ndkPath)
  private val prefix: String = androidAbi.toNdkToolchainName

  private val arName: String = "llvm-ar"
  val linker: File =
    new File(binDir, s"$prefix$androidApi-clang")
  val ar: File = new File(binDir, arName)

  require(linker.exists())
  require(ar.exists())
}
