//package utils
//
//import java.io.{File, RandomAccessFile}
//import collection.mutable
//import core.{Tile, Tileset, Race}
//import sbinary.{Operations, DefaultProtocol}
//import models._
//import models.Unit
//
//object PudParser {
//  def parse(name: String): Pud = {
//    val pudFile: RandomAccessFile = new RandomAccessFile("conf" + File.separatorChar + "maps" + File.separatorChar + name, "r")
//
//    val sections = mutable.HashMap.empty[String, Array[Byte]]
//
//    while (pudFile.getFilePointer < pudFile.length()) {
//      val sectionName = nextString(pudFile, 4)
//      val length: Int = Integer.reverseBytes(pudFile.readInt())
//      val b = new Array[Byte](length)
//      pudFile.read(b)
//      sections += (sectionName -> b)
//    }
//
//    implicit val unitReads = UnitReads
//
//    val dim: Array[Byte] = sections.get("DIM ").get
//    new Pud(new String(sections.get("DESC").get),
//      sections.get("OWNR").get map PlayerTypes.valueOf,
//      sections.get("SIDE").get map { Race(_) },
//      Tileset(makeShort(sections.get("ERA ").get)),
//      makeShort(dim.slice(0, 2)), makeShort(dim.slice(2, 4)),
//      sections.get("MTXM").get.sliding(2, 2).toArray map { ba => new Tile(makeShort(ba).toInt) },
//      sections.get("UNIT").get.sliding(8, 8).toArray map Operations.fromByteArray[Unit],
//      sections.get("AIPL").get map { AiType(_) },
//      sections.get("SGLD").get.sliding(2, 2).toArray map makeShort,
//      sections.get("SLBR").get.sliding(2, 2).toArray map makeShort,
//      sections.get("SOIL").get.sliding(2, 2).toArray map makeShort,
//      sections.get("UDTA") map UnitData.valueOf,
//      sections.get("UGRD") map UpgradeData.valueOf,
//      sections.get("SQM ").get.sliding(2).toArray map { ba => new Block(makeShort(ba)) },
//      sections.get("REGM").get.sliding(2).toArray map { ba => new ActionBlock(makeShort(ba)) }
//    )
//  }
//
//  def nextString(file: RandomAccessFile, length: Int): String = {
//    val b : Array[Byte] = new Array[Byte](length)
//    file.read(b)
//    new String(b)
//  }
//
//  def makeShort(ba: Array[Byte]): Short = {
//    (makeUnsigned(ba(0)) + (makeUnsigned(ba(1)) << 8)).toShort
//  }
//
//  def makeInt(ba: Array[Byte]): Int = {
//    makeUnsigned(ba(0)) + makeUnsigned(ba(1)) << 8 +
//    makeUnsigned(ba(2)) << 16 + makeUnsigned(ba(3)) << 24
//  }
//
//  def makeBoolean(b: Byte): Boolean = b match { case 0 => false; case _ => true }
//
//  def makeUnsigned(b: Byte): Int = {
//    (b & 0x7F) + (b & 0x80)
//  }
//}
