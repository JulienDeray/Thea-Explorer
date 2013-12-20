package core

import java.util.Properties
import java.io.{FileOutputStream, FileInputStream}

class ConfigManager {

  def apply(key: String) = getProperties.getProperty(key)

  private def getProperties: Properties = {
    val prop = new Properties
    prop.load( new FileInputStream("conf/customParameters.properties") )
    prop
  }

  def saveProperties(properties: Properties) {
    properties.store(new FileOutputStream("conf/customParameters.properties"), null)
  }

  def addProperty(key: String, value: String): Properties = {
    val prop = getProperties
    prop.setProperty(key, value)
    prop
  }
}
