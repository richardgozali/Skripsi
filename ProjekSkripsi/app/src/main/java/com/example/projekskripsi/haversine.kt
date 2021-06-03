package com.example.projekskripsi
import java.lang.Math.*

class haversine {

    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val λ1 = toRadians(lat1)
        val λ2 = toRadians(lat2)
        val Δλ = toRadians(lat2 - lat1)
        val Δφ = toRadians(lon2 - lon1)
        return 2 * Companion.R * asin(sqrt(pow(sin(Δλ / 2), 2.0) + pow(sin(Δφ / 2), 2.0) * cos(λ1) * cos(λ2)))
    }

    fun main(args: Array<String>) = println("result: " + haversine(36.12, -86.67, 33.94, -118.40))

    companion object {
        const val R = 6372.8 // in kilometers
    }
}