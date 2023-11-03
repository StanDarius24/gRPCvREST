package com.stannis.grpc.server;

import com.stannis.grpc.StudentRequest;
import com.stannis.grpc.StudentResponse;
import com.stannis.grpc.StudentServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GRpcService
public class Server extends StudentServiceGrpc.StudentServiceImplBase{

    Logger logger = LoggerFactory.getLogger(Server.class);
    StudentResponse response = StudentResponse.newBuilder()
            .setAge(70).setName("Serghei Mizil")
            .build();
    @Override
    public void getStudent(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        logger.info("got request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        logger.info("sent response");
    }

    @Override
    public void getStudentStream(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        logger.info("got request");
        long startTime = System.nanoTime();
        for(int i=0;i<1000000;i++){
            responseObserver.onNext(response);
        }
        long endTime = System.nanoTime();
        long durationInNano = endTime - startTime;
        double durationInMillis = (double) durationInNano / 1_000_000;
        responseObserver.onCompleted();
        logger.info("Execution time: " + durationInMillis + " milliseconds");
    }

    @Override
    public StreamObserver<StudentRequest> sendStudentStream(StreamObserver<StudentResponse> responseObserver) {
        logger.info("got request");
        long startTime = System.nanoTime();
        long endTime = System.nanoTime();
        var responseStream = new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest studentRequest) {

            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("got some error "+ throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                long durationInNano = endTime - startTime;
                double durationInMillis = (double) durationInNano / 1_000_000;
                logger.info("Execution time: " + durationInMillis + " milliseconds");
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
        logger.info("sent response");
        return responseStream;
    }


    public StreamObserver<StudentRequest> sendAndGetStudentStream(StreamObserver<StudentResponse> responseObserver) {
        logger.info("going to send and get");
        long startTime = System.nanoTime();
        long endTime = System.nanoTime();

        var res = new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest studentRequest) {
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("got error = "+ throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                long durationInNano = endTime - startTime;
                double durationInMillis = (double) durationInNano / 1_000_000;
                logger.info("Execution time: " + durationInMillis + " milliseconds");
                responseObserver.onCompleted();
            }
        };

        return res;

    }

}
