import io.grpc.stub.StreamObserver
import vehicle.FillTruckGrpc
import vehicle.TruckOuterClass

class TruckTest: FillTruckGrpc.FillTruckImplBase() {

    override fun fillTruck(request: TruckOuterClass.Truck?, responseObserver: StreamObserver<TruckOuterClass.Truck>?) {
        super.fillTruck(request, responseObserver)
    }

    fun test() {
        val truck = TruckOuterClass.Truck.newBuilder()
            .setVin("123")
            .setTrailer(
                TruckOuterClass.Trailer.newBuilder()
                    .setColour("blue")
                    .setCapacity(123)
                    .build()
            )
            .setName("asw")
            .build()
    }

}
