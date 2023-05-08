import LogFuncs.checkSpeed
import org.scalatest.funsuite.AnyFunSuite
import java.lang.Thread.sleep

class MiscTestSuite extends AnyFunSuite {

  test("Checkspeed should print 1000 to console and log it") {

    checkSpeed("Test function checkspeed")(sleep(1000))

  }


}