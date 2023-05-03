package pers.zhc.gradle.plugins.ndk

case class Target(abi: AndroidAbi, api: Int)

object Target {
  type Targets = List[Target]
}
