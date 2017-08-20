/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nz.mega.sdk;

class MegaGlobalListener {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected MegaGlobalListener(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(MegaGlobalListener obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  protected synchronized void delete() {   
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        megaJNI.delete_MegaGlobalListener(swigCPtr);
      }
      swigCPtr = 0;
    }
}

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    megaJNI.MegaGlobalListener_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    megaJNI.MegaGlobalListener_change_ownership(this, swigCPtr, true);
  }

  public void onUsersUpdate(MegaApi api, MegaUserList users) {
    if (getClass() == MegaGlobalListener.class) megaJNI.MegaGlobalListener_onUsersUpdate(swigCPtr, this, MegaApi.getCPtr(api), api, MegaUserList.getCPtr(users), users); else megaJNI.MegaGlobalListener_onUsersUpdateSwigExplicitMegaGlobalListener(swigCPtr, this, MegaApi.getCPtr(api), api, MegaUserList.getCPtr(users), users);
  }

  public void onNodesUpdate(MegaApi api, MegaNodeList nodes) {
    if (getClass() == MegaGlobalListener.class) megaJNI.MegaGlobalListener_onNodesUpdate(swigCPtr, this, MegaApi.getCPtr(api), api, MegaNodeList.getCPtr(nodes), nodes); else megaJNI.MegaGlobalListener_onNodesUpdateSwigExplicitMegaGlobalListener(swigCPtr, this, MegaApi.getCPtr(api), api, MegaNodeList.getCPtr(nodes), nodes);
  }

  public void onAccountUpdate(MegaApi api) {
    if (getClass() == MegaGlobalListener.class) megaJNI.MegaGlobalListener_onAccountUpdate(swigCPtr, this, MegaApi.getCPtr(api), api); else megaJNI.MegaGlobalListener_onAccountUpdateSwigExplicitMegaGlobalListener(swigCPtr, this, MegaApi.getCPtr(api), api);
  }

  public void onContactRequestsUpdate(MegaApi api, MegaContactRequestList requests) {
    if (getClass() == MegaGlobalListener.class) megaJNI.MegaGlobalListener_onContactRequestsUpdate(swigCPtr, this, MegaApi.getCPtr(api), api, MegaContactRequestList.getCPtr(requests), requests); else megaJNI.MegaGlobalListener_onContactRequestsUpdateSwigExplicitMegaGlobalListener(swigCPtr, this, MegaApi.getCPtr(api), api, MegaContactRequestList.getCPtr(requests), requests);
  }

  public void onReloadNeeded(MegaApi api) {
    if (getClass() == MegaGlobalListener.class) megaJNI.MegaGlobalListener_onReloadNeeded(swigCPtr, this, MegaApi.getCPtr(api), api); else megaJNI.MegaGlobalListener_onReloadNeededSwigExplicitMegaGlobalListener(swigCPtr, this, MegaApi.getCPtr(api), api);
  }

  public void onChatsUpdate(MegaApi api, MegaTextChatList chats) {
    if (getClass() == MegaGlobalListener.class) megaJNI.MegaGlobalListener_onChatsUpdate(swigCPtr, this, MegaApi.getCPtr(api), api, MegaTextChatList.getCPtr(chats), chats); else megaJNI.MegaGlobalListener_onChatsUpdateSwigExplicitMegaGlobalListener(swigCPtr, this, MegaApi.getCPtr(api), api, MegaTextChatList.getCPtr(chats), chats);
  }

  public void onEvent(MegaApi api, MegaEvent event) {
    if (getClass() == MegaGlobalListener.class) megaJNI.MegaGlobalListener_onEvent(swigCPtr, this, MegaApi.getCPtr(api), api, MegaEvent.getCPtr(event), event); else megaJNI.MegaGlobalListener_onEventSwigExplicitMegaGlobalListener(swigCPtr, this, MegaApi.getCPtr(api), api, MegaEvent.getCPtr(event), event);
  }

  public MegaGlobalListener() {
    this(megaJNI.new_MegaGlobalListener(), true);
    megaJNI.MegaGlobalListener_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

}
