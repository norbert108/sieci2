#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>

#include <netinet/in.h>
#include <sys/un.h>
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

    // struct sockaddr_un me;
    // me.sun_family = AF_UNIX;
    // bind(server_socket, (void*)&me, sizeof(short));

    int conn_status = connect(server_socket, addr, addrlen);     
    perror("Connection");
    if(conn_status){
        exit(errno);
    }

    /* send requests to server */
    char var1B = 10;
    short var2B = 11;
    int var4B = 12;
    long long var8B = 13;

    char dataLen = 0;

    printf("Sending %d as 1B of data...\n", var1B);
    dataLen = 1;
    if(send(server_socket, &dataLen, sizeof(dataLen), 0) == -1){
        perror("");
    }
    if(send(server_socket, &var1B, sizeof(var1B), 0) == -1){
        perror("send 1B");
    }

    printf("Sending %d as 2B of data...\n", var2B);
    dataLen = 2;
    if(send(server_socket, &dataLen, sizeof(dataLen), 0) == -1){
        perror("");
    }
    if(send(server_socket, &var2B, sizeof(var2B), 0) == -1){
        perror("send 2B");
    }

    printf("Sending %d as 4B of data...\n", var4B);
    dataLen = 4;
    if(send(server_socket, &dataLen, sizeof(dataLen), 0) == -1){
        perror("");
    }
    if(send(server_socket, &var4B, sizeof(var4B), 0) == -1){
        perror("send 4B");
    }

    printf("Sending %lld as 8B of data...\n", var8B);
    dataLen = 8;
    if(send(server_socket, &dataLen, sizeof(dataLen), 0) == -1){
        perror("");
    }
    if(send(server_socket, &var8B, sizeof(var8B), 0) == -1){
        perror("send 8B");
    }

    fflush(stdout);

    close (server_socket);
    return 0;
}

