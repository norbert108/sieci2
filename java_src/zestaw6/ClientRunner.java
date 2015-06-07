public class ClientRunner {

    public static void main(String[] args) {

//        if (args.length < 4) {
//            System.err.println("USAGE: Executor hostPort znode filename program [args ...]");
//            System.exit(2);
//        }

        String hostPort = args[0];
        String filename = args[1];
//        String exec[] = new String[args.length - 3];
//        System.arraycopy(args, 3, exec, 0, exec.length);

        try {
            new Executor(hostPort, filename, null).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
