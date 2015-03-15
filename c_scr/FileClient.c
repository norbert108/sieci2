#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <arpa/inet.h>

int main ()
{
    int server_socket = socket (AF_INET, SOCK_STREAM, 0);
    if(server_socket < 0){
        perror("socket");
        exit(errno);
    }

    /* bind socket to address */
    struct sockaddr_in remote_addr;
    memset(&remote_addr, 0, sizeof(struct sockaddr_in));

    remote_addr.sin_family = AF_INET;
    remote_addr.sin_port = htons(9999);
    if(inet_pton(AF_INET, "127.0.0.1", &remote_addr.sin_addr) < 1){
        printf("\"%s\" is not valid ipv4 address\n", "ff");
        exit(1);
    }

    struct sockaddr* addr = (struct sockaddr*)&remote_addr;
    socklen_t addrlen = sizeof(struct sockaddr_in);

    int conn_status = connect(server_socket, addr, addrlen);     
    perror("Connection");
    if(conn_status){
        exit(errno);
    }

    /* send requests to server */
    char* filename = "dupa";
    char dataLen = strlen(filename);

    printf("Sending request for file %s...\n", filename);
    if(send(server_socket, &dataLen, sizeof(dataLen), 0) == -1){
        perror("");
    }
    if(send(server_socket, filename, sizeof(filename), 0) == -1){
        perror("send file name");
    }

    char result;
    recv(server_socket, &result, 1, 0);

    
    if (result == 0){
        char* fileSizeBytes = malloc(4);
        recv(server_socket, fileSizeBytes, 4, 0);

        long fileSize = 0;
        fileSize = (fileSizeBytes[0] << 24) |
                    (fileSizeBytes[1] << 16) |
                    (fileSizeBytes[2] << 8) |
                    fileSizeBytes[3]; 

        char* receivedFileBytes = malloc(fileSize);
        recv(server_socket, receivedFileBytes, fileSize, 0);

        FILE* receivedFile = fopen(filename, "wb");
        fwrite(receivedFileBytes, 1, fileSize, receivedFile);

        printf("File \"%s\" sucessfully received.\n", filename);

        fclose(receivedFile);
    } else if(result == 1){
        printf("File \"%s\" not found on server!\n", filename);
    } else {
        printf("Unknown transfer error\n");
    }

    fflush(stdout);

    close (server_socket);
    return 0;
}

