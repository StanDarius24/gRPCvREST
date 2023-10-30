import vehicle.CarOuterClass
import vehicle.TruckOuterClass

fun main(args: Array<String>) {

    val car = CarOuterClass.Car.newBuilder()
        .setKm(123)
        .setName("Mercedes")
        .setVin("abcd")
        .build()

    val truck = TruckOuterClass.Truck.newBuilder()
        .setKm(123)
        .setName("Mercedes")
        .setVin("truck123")
        .setTrailer(
            TruckOuterClass.Trailer.newBuilder()
                .setCapacity(123)
                .setColour("blue")
                .build()
        )
        .build()
    println(car.toString())
    println(truck.toString())

}
