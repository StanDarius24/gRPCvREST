const grpc = require('grpc');
const protoLoader = require('@grpc/proto-loader');
const readline = require('readline');

const packageDefinition = protoLoader.loadSync('proto/Order.proto', {
    keepCase: true,
    longs: String,
    enums: String,
    defaults: true,
    oneofs: true,
});

const protoDescriptor = grpc.loadPackageDefinition(packageDefinition);
const studentService = protoDescriptor.StudentService;

const client = new studentService('localhost:6565', grpc.credentials.createInsecure());

function main() {


    const studentRequest = { id: 1 };

    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });

    rl.question("Select a choice: \n1 for no streaming \n2 for server side streaming \n3 for client side streaming\n4 for bidirectional streaming\n", (choice) => {
        switch (parseInt(choice)) {
            case 1:
                getStudents(client, studentRequest);
                break;
            case 2:
                getStudentsStream(client, studentRequest);
                break;
            case 3:
                sendUserStream(client, studentRequest);
                break;
            case 4:
                getAndSendStudent(client, studentRequest);
                break;
            default:
                console.error("Invalid choice, please enter again.");
                main();
        }
        rl.close();
    });

    // Graceful shutdown
    process.on('SIGINT', () => {
        client.close();
        process.exit(0);
    });
}

function getStudents(client, studentRequest) {
    const startTime = process.hrtime();

    // Assuming `getStudent` is a unary gRPC call
    client.getStudent(studentRequest, (err, response) => {
        if (err) {
            console.error('Error:', err);
        } else {
            // Perform the same gRPC call again to simulate the synchronous behavior
            client.getStudent(studentRequest, (err, response) => {
                if (err) {
                    console.error('Error:', err);
                } else {
                    console.log(studentRequest)
                    const endTime = process.hrtime(startTime);
                    const totalTimeInMs = (endTime[0] * 1000 + endTime[1] / 1000000).toFixed(2);
                    console.log('Total time =', totalTimeInMs, 'ms');
                }
            });
        }
    });
}

function getStudentsStream(client, studentRequest) {
    const startTime = process.hrtime();

    console.log('Going to call the gRPC service.');

    const call = client.getStudentStream(studentRequest);

    call.on('data', (studentResponse) => {

    });

    call.on('error', (err) => {
        console.error('Error:', err);
    });

    call.on('end', () => {
        const endTime = process.hrtime(startTime);
        const totalTimeInMs = (endTime[0] * 1000 + endTime[1] / 1000000).toFixed(2);
        console.log('Completed');
        console.log('Total time =', totalTimeInMs, 'ms');
    });
}

function sendUserStream(client, studentRequest) {
    const startTime = process.hrtime();

    console.log('Going to call the gRPC service.');

    const call = client.sendStudentStream((error, response) => {
        if (error) {
            console.error('Error:', error);
        } else {
            console.log('Completed');
            const endTime = process.hrtime(startTime);
            const totalTimeInMs = (endTime[0] * 1000 + endTime[1] / 1000000).toFixed(2);
            console.log('Total time =', totalTimeInMs, 'ms');
        }
    });

    call.on('error', (err) => {
        console.error('Error:', err);
    });

    for (let i = 0; i < 1000000; i++) {
        call.write(studentRequest);
    }

    call.end(); // Indicates the end of sending data
}

function getAndSendStudent(client, studentRequest) {
    const startTime = process.hrtime();

    console.log('Going to call the gRPC service.');

    const call = client.sendAndGetStudentStream();

    call.on('data', (studentResponse) => {
        // Handle the received student response as needed
    });

    call.on('error', (err) => {
        console.error('Error:', err);
    });

    call.on('end', () => {
        console.log('Completed');
        const endTime = process.hrtime(startTime);
        const totalTimeInMs = (endTime[0] * 1000 + endTime[1] / 1000000).toFixed(2);
        console.log('Total time =', totalTimeInMs, 'ms');
    });

    for (let i = 0; i < 1000000; i++) {
        call.write(studentRequest);
    }

    call.end(); // Indicates the end of sending data
}

main(); // Start the program
