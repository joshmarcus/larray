//--------------------------------------
//
// LArrayLoaderTest.scala
// Since: 2013/03/29 9:56
//
//--------------------------------------

package xerial.larray.impl

import xerial.larray.LArraySpec
import java.net.{URL, URLClassLoader}
import java.io.File

/**
 * @author Taro L. Saito
 */
class LArrayLoaderTest extends LArraySpec {

  "LArrayLoader" should {

    "use a different library name for each class loader" in {

      val cl = Thread.currentThread().getContextClassLoader()
      val parentCl = cl.getParent
      val classPath = Array(new File("target/classes").toURI.toURL)
      val cl1 = new URLClassLoader(classPath, parentCl)
      val cl2 = new URLClassLoader(classPath, parentCl)

      import java.{lang=>jl}

      val nativeCls1 = cl1.loadClass("xerial.larray.impl.LArrayNative")
      val ni1 = nativeCls1.newInstance()
      val arr1 = Array.ofDim[Byte](100)
      val m1 = nativeCls1.getDeclaredMethod("copyToArray", jl.Long.TYPE, classOf[AnyRef], jl.Integer.TYPE, jl.Integer.TYPE)
      m1.invoke(ni1, Seq.apply[AnyRef](new jl.Long(0L), arr1, new jl.Integer(0), new jl.Integer(0)):_*)

      val nativeCls1_2 = cl1.loadClass("xerial.larray.impl.LArrayNative")

      val nativeCls2 = cl2.loadClass("xerial.larray.impl.LArrayNative")
      val ni2 = nativeCls2.newInstance()
      val arr2 = Array.ofDim[Byte](100)
      val m2 = nativeCls2.getDeclaredMethod("copyToArray", jl.Long.TYPE, classOf[AnyRef], jl.Integer.TYPE, jl.Integer.TYPE)
      m2.invoke(ni1, Seq.apply[AnyRef](new jl.Long(0L), arr2, new jl.Integer(0), new jl.Integer(0)):_*)

      nativeCls1 should not be (nativeCls2)
      nativeCls1 should be (nativeCls1_2)

      val arr3 = Array.ofDim[Byte](100)
      LArrayNative.copyToArray(0, arr3, 0, 0)
    }


  }

}