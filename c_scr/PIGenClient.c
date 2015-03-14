#include <stdio.h>
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
    printf("XD");
    fflush(stdout);
    int server_socket = socket (AF_INET, SOCK_STREAM, 0);
    if(server_socket < 0){
        perror("socket");
        // exit(errno);
    }

    // /* bind socket to address */
    struct sockaddr_in remote_addr;
    memset(&remote_addr, 0, sizeof(struct sockaddr_in));

    remote_addr.sin_family = AF_INET;

    // /* port */
    remote_addr.sin_port = htons(9999);

    // /* domain */
    if(inet_pton(AF_INET, "127.0.0.1", &remote_addr.sin_addr) < 1){
        // printf("\"%s\" is not valid ipv4 address\n", argv[2]);
        // exit(1);
    }

    struct sockaddr* addr = (struct sockaddr*)&remote_addr;
    socklen_t addrlen = sizeof(struct sockaddr_in);

    struct sockaddr_un me;
    me.sun_family = AF_UNIX;
    bind(server_socket, (void*)&me, sizeof(short));

    if(!connect(server_socket, addr, addrlen)){}

    int xd = 1;
    ssize_t retval = send(server_socket, &xd, sizeof(xd), 0);
    printf("Return: %d", retval);
    fflush(stdout);

    // close (server_socket);
    return 0;
}

