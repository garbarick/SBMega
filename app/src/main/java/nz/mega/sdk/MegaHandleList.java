/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nz.mega.sdk;

public class MegaHandleList {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected MegaHandleList(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(MegaHandleList obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  protected synchronized void delete() {   
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        megaJNI.delete_MegaHandleList(swigCPtr);
      }
      swigCPtr = 0;
    }
}

  public static MegaHandleList createInstance() {
    long cPtr = megaJNI.MegaHandleList_createInstance();
    return (cPtr == 0) ? null : new MegaHandleList(cPtr, false);
  }

   MegaHandleList copy() {
    long cPtr = megaJNI.MegaHandleList_copy(swigCPtr, this);
    return (cPtr == 0) ? null : new MegaHandleList(cPtr, false);
  }

  public long get(long i) {
    return megaJNI.MegaHandleList_get(swigCPtr, this, i);
  }

  public long size() {
    return megaJNI.MegaHandleList_size(swigCPtr, this);
  }

  public void addMegaHandle(long megaHandle) {
    megaJNI.MegaHandleList_addMegaHandle(swigCPtr, this, megaHandle);
  }

  public MegaHandleList() {
    this(megaJNI.new_MegaHandleList(), true);
  }

}