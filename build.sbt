name := "GridEngine"

version := "0.1"

scalaVersion := "2.13.0"

libraryDependencies ++= {
  val version = "3.2.2"
  val os = "linux" // TODO: Change to "linux" or "macos" if necessary

  Seq(
    "lwjgl",
    "lwjgl-glfw",
    "lwjgl-opengl",
    "lwjgl-openal",
    "lwjgl-stb",
  ).flatMap {
    module => {
      Seq(
        "org.lwjgl" % module % version,
        "org.lwjgl" % module % version classifier s"natives-$os"
      )
    }
  }
}

