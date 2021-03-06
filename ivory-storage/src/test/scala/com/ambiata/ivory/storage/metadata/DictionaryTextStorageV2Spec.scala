package com.ambiata.ivory.storage.metadata

import com.ambiata.ivory.core._
import com.ambiata.ivory.core.arbitraries.Arbitraries._
import org.specs2.{ScalaCheck, Specification}
import scalaz._, Scalaz._

class DictionaryTextStorageV2Spec extends Specification with ScalaCheck { def is = s2"""
  Parsing a dictionary on:
    Invalid inputs will result in a failure     $invalidInput
    Parse any valid dictionary                  $anyDictionary
"""

  import DictionaryTextStorageV2._

  def invalidInput = seqToResult(List(
    "invalid",
    "encoding=string",
    "invalid|encoding=string",
    "a:b",
    "a:b|encoding=abc",
    "a:b|encoding=string|type=xxx",
    "a:b|source=xy",
    "a:b|source=x:y",
    "a:b|source=x:y|encoding=string",
    "a:b|source=x:y|window=foo",
    "a:b|source=x:y|window=m days",
    "a:b|source=x:y|window=4 dayz",
    "a:b|source=x:y|expression=bad",
    "a:b|encoding=string|mode=bad",
    "a:b|encoding=string|encoding=int"
  ).map(fromString).map(_.toEither must beLeft))

  def anyDictionary = prop((dict: Dictionary) =>
    fromString(delimitedString(dict)) ==== dict.success
  )
}
