package com.rndash.mbheadunit.doom.wad.mapData

import com.rndash.mbheadunit.doom.wad.Patch
import com.rndash.mbheadunit.doom.wad.Texture
import com.rndash.mbheadunit.doom.wad.WadFile
import java.nio.ByteBuffer

const val THING_SIZE_BYTES = 10
const val LINDEF_SIZE_BYTES = 14
const val SIDEDEF_SIZE_BYTES = 30
const val VERTEX_SIZE_BYTES = 4
const val SEG_SIZE_BYTES = 12
const val SUBSECTOR_SIZE_BYTES = 4
const val BBOX_SIZE_BYTES = 8
const val NODE_SIZE_BYTES = 8 + (2* BBOX_SIZE_BYTES) + 4
const val SECTOR_SIZE_BYTES = 26

@ExperimentalUnsignedTypes
class Level(
    val name: String,
    val things: Array<Thing>,
    val lineDefs: Array<LineDef>,
    val sideDefs: Array<SideDef>,
    val vertexes: Array<Vertex>,
    val segs: Array<Seg>,
    val subSectors: Array<SubSector>,
    val nodes: Array<Node>,
    val sectors: Array<Sector>
) {
    val flats = HashMap<String, ByteBuffer>()
    fun cacheFlats(w: WadFile) {
        sectors.forEach {
            cacheFlat(it.ceilingPic, w)
            cacheFlat(it.floorPic, w)
        }
    }

    private fun cacheFlat(name: String, w: WadFile) {
        if (name != "-" && flats[name] == null) {
            println("Caching $name")
            try {
                flats[name] = w.readFlat(name)
            } catch (e: Exception) {
                System.err.println("Patch $name not found for ${this.name}")
            }
        }
    }
}

class Flat(val data: ByteArray)

class Thing(
    val xPos: Short,
    val yPos: Short,
    val angle: Short,
    val type: Short,
    val flags: Short
)

class LineDef(
    val vertexStart: Short,
    val vertexEnd: Short,
    val flags: Short,
    val function: Short,
    val tag: Short,
    val sideDefRight: Short,
    val sideDefLeft: Short
)

class SideDef(
    val xOffset: Short,
    val yOffset: Short,
    val uppperTexture: String,
    val lowerTexture: String,
    val middleTexture: String,
    val sectorRef: Short
)

class Vertex(
    val x: Short,
    val y: Short,
) {
    // Needed for Shape Mesh building
    override fun equals(other: Any?): Boolean {
        if (other is Vertex) {
            return other.x == x && other.y == y
        }
        return false
    }
}

class Seg(
    val vertexStart: Short,
    val vertexEnd: Short,
    val bams: Short,
    val lineNum: Short,
    val segSide: Short,
    val segOffset: Short
)

class SubSector(
    val numSegs: Short,
    val startSeg: Short
)

class Bbox(
    val top: Short,
    val bottom: Short,
    val left: Short,
    val right: Short
)

class Node(
    val x: Short,
    val y: Short,
    val dx: Short,
    val dy: Short,
    val bbox: Array<Bbox>,
    val child: ShortArray
) {
    init {
        require(bbox.size == 2)
        require(child.size == 2)
    }

    override fun toString(): String {
        return "Node: ($x, $y) - $($dx, $dy) - (${x+dx}, ${y+dy})"
    }
}

class Sector(
    val floorHeight: Short,
    val ceilingHeight: Short,
    val floorPic: String,
    val ceilingPic: String,
    val lightLevel: Short,
    val specialSector: Short,
    val tag: Short
)