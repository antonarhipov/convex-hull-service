package org.geometry.convexhull

import org.geometry.math.D
import org.geometry.math.TheAlgorithm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AlgorithmController {
    @PostMapping("/ds")
    fun createDList(@RequestBody ds: MutableList<D?>): ResponseEntity<MutableList<D?>?> {
        val input = ds.toTypedArray<D?>()
        val result = TheAlgorithm.run(input)
        println("The convex contains " + result!!.size + " points")
        return ResponseEntity.ok<MutableList<D?>?>(result)
    }
}
