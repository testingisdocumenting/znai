import static com.twosigma.testing.tcli.TcliDsl.*

scenario("updating deployment with a specified version") {
    runCli("tool deploy --version=123") {
        on("preparing deployment") {

        }

        output.should contain("test line")
        exitCode.should == 3
    }
}