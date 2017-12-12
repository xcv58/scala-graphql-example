package com.xcv58.graphql

import com.fasterxml.jackson.databind.JsonNode
import com.twitter.finatra.json.FinatraObjectMapper
import org.json4s.JsonAST.{JArray, JNull, JObject, JValue}
import org.json4s.jackson.JsonMethods
import org.json4s.jackson.JsonMethods.asJsonNode
import sangria.marshalling.json4s.jackson.Json4sJacksonResultMarshaller

object JsonUtils {
  // Basic JSON mapper
  val mapper: FinatraObjectMapper = FinatraObjectMapper.create()

  def removeObjectNulls(node: JValue): JValue = node match {
    // Strip out null values from the values of objects
    case JObject(fields) =>
      JObject(fields flatMap {
        case (k: String, JNull) => None
        case (k: String, v: JValue) => Some((k, removeObjectNulls(v)))
      })

    // We don't want to strip null values from the items in an array,
    // but the array might contain objects that contain nulls, so we recurse.
    case JArray(values) => JArray(values map removeObjectNulls)

    // Everything else is fine
    case _ => node
  }

  def toJson(node: Json4sJacksonResultMarshaller.Node): JsonNode =
    asJsonNode(removeObjectNulls(node))

  // We have problem to directly parse variables to JObject
  def toJValue(map: Option[Map[String, Any]]): JValue = map match {
    case Some(m) => JsonMethods.parse(mapper.writeValueAsString(m))
    case _ => JObject()
  }
}
