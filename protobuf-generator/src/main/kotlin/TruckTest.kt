import io.grpc.stub.StreamObserver
import vehicle.FillTruckGrpc
import vehicle.TruckOuterClass

class TruckTest: FillTruckGrpc.FillTruckImplBase() {

    override fun fillTruck(request: TruckOuterClass.Truck?, responseObserver: StreamObserver<TruckOuterClass.Truck>?) {
        super.fillTruck(request, responseObserver)
    }

}
