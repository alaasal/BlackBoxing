// See LICENSE.txt for license details.
package Adder

import java.io.File
import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}


class AdderUnitTester(c: Adder) extends PeekPokeTester(c) {
  for (t <- 0 until 4) {
    val rnd0 = rnd.nextInt(c.n)
    val rnd1 = rnd.nextInt(c.n)
    val rnd2 = rnd.nextInt(1)

    poke(c.io.A, rnd0)
    poke(c.io.B, rnd1)
    poke(c.io.Cin, rnd2)
    step(1)
    val rsum = rnd0 + rnd1 + rnd2
    val mask = BigInt("1"*c.n, 2)
    expect(c.io.Sum, rsum &  mask)
    expect(c.io.Cout, rsum % 1)
}
}
/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly Adder.AdderTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly Adder.AdderTester'
  * }}}
  */
class AdderTester extends ChiselFlatSpec {
  // Disable this until we fix isCommandAvailable to swallow stderr along with stdout
  private val backendNames = if(firrtl.FileUtils.isCommandAvailable(Seq("verilator", "--version"))) {
    Array("firrtl", "verilator")
  }
  else {
    Array("firrtl")
  }
  for ( backendName <- backendNames ) {
    "Adder" should s"calculate proper greatest common denominator (with $backendName)" in {
      Driver(() => new Adder(2), backendName) {
        c => new AdderUnitTester(c)
      } should be (true)
    }
  }

  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new Adder(2)) {
      c => new AdderUnitTester(c)
    } should be (true)
  }

  if(backendNames.contains("verilator")) {
    "using --backend-name verilator" should "be an alternative way to run using verilator" in {
      iotesters.Driver.execute(Array("--backend-name", "verilator"), () => new Adder(2)) {
        c => new AdderUnitTester(c)
      } should be(true)
    }
  }

  "running with --is-verbose" should "show more about what's going on in your tester" in {
    iotesters.Driver.execute(Array("--is-verbose"), () => new Adder(2)) {
      c => new AdderUnitTester(c)
    } should be(true)
  }

  /**
    * By default verilator backend produces vcd file, and firrtl and treadle backends do not.
    * Following examples show you how to turn on vcd for firrtl and treadle and how to turn it off for verilator
    */

  "running with --generate-vcd-output on" should "create a vcd file from your test" in {
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "on", "--target-dir", "test_run_dir/make_a_vcd", "--top-name", "make_a_vcd"),
      () => new Adder(2)
    ) {

      c => new AdderUnitTester(c)
    } should be(true)

    new File("test_run_dir/make_a_vcd/make_a_vcd.vcd").exists should be (true)
  }

  "running with --generate-vcd-output off" should "not create a vcd file from your test" in {
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "off", "--target-dir", "test_run_dir/make_no_vcd", "--top-name", "make_no_vcd",
      "--backend-name", "verilator"),
      () => new Adder(2)
    ) {

      c => new AdderUnitTester(c)
    } should be(true)

    new File("test_run_dir/make_no_vcd/make_a_vcd.vcd").exists should be (false)

  }

}
