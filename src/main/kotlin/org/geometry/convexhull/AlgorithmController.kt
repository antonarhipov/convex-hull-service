package org.geometry.convexhull

import org.geometry.convexhull.ext.Coordinate
import org.geometry.convexhull.ext.JarvisMarch
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.collections.toTypedArray

@RestController
class AlgorithmController {
    @PostMapping("/ds")
    fun createDList(@RequestBody ds: List<Coordinate>): ResponseEntity<List<Coordinate>> {
        val input = ds.toTypedArray()
        val result = JarvisMarch().run(ds)
        println("The convex contains " + result.size + " points")
        return ResponseEntity.ok(result)
    }
}
