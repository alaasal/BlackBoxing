// See LICENSE.txt for license details.
package Adder

import chisel3._
import chisel3.util._
import chisel3.experimental._

/*class FullAdder extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val a    = Input(UInt(1.W))
    val b    = Input(UInt(1.W))
    val cin  = Input(UInt(1.W))
    val sum  = Output(UInt(1.W))
    val cout = Output(UInt(1.W))
  })

  setInline("FullAdder.v",
    s"""
      |module BlackBoxRealAdd(
      | input a,
      | input b,
      | input cin,
      | output sum,
      | output cout
      |);
      | assign  sum = a | b | cin;
      | assign cout = a&b | a&cin | b&cin;
      |endmodule
    """.stripMargin)



}*/
/*  // Generate the sum
  val a_xor_b = io.a ^ io.b
  io.sum := a_xor_b ^ io.cin
  // Generate the carry
  val a_and_b = io.a & io.b
  val b_and_cin = io.b & io.cin
  val a_and_cin = io.a & io.cin
  io.cout := a_and_b | b_and_cin | a_and_cin
}*/


class FullAdder extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val a    = Input(UInt(1.W))
    val b    = Input(UInt(1.W))
    val cin  = Input(UInt(1.W))
    val sum  = Output(UInt(1.W))
    val cout = Output(UInt(1.W))
  })

  setInline("FullAdder.v",
    s"""
|
|   module top(
|    input a,
|    input b,
|    input cin,
|    output sum,
|    output cout);    
|   
|   
|   assign sum = (a ^ b ^ cin);
|   assign cout = ( (a&&b) || (a&&cin) || (b&cin) ); endgenerate  
| 
| endmodule
""".stripMargin)

}