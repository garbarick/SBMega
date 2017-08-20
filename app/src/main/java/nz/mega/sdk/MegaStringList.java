/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nz.mega.sdk;

public class MegaStringList {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected MegaStringList(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(MegaStringList obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  protected synchronized void delete() {   
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        megaJNI.delete_MegaStringList(swigCPtr);
      }
      swigCPtr = 0;
    }
}

   MegaStringList copy() {
    long cPtr = megaJNI.MegaStringList_copy(swigCPtr, this);
    return (cPtr == 0) ? null : new MegaStringList(cPtr, false);
  }

  public String get(int i) {
    return megaJNI.MegaStringList_get(swigCPtr, this, i);
  }

  public int size() {
    return megaJNI.MegaStringList_size(swigCPtr, this);
  }

  public MegaStringList() {
    this(megaJNI.new_MegaStringList(), true);
  }

}
