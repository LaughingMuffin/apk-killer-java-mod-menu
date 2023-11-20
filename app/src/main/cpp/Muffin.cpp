//
// Modified by Laughing Muffin on 15/11/2023
//

#include <iostream>
#include <jni.h>
//==================================================================================================
#include <list>
#include <vector>
#include <cstring>
#include <pthread.h>
#include <thread>
#include <cstring>
#include <jni.h>
#include <unistd.h>
#include <fstream>
#include <iostream>
#include <dlfcn.h>
#include "Includes/Logger.h"
#include "Includes/obfuscate.h"
#include "Utils.h"
#include "menu/Menu.h"

//==================================================================================================
#include "thirdParty/APKKiller.h"

//==================================================================================================
void *hack_thread(void *) {
    Debug_Log(OBFUSCATE("pthread created"));

    //Check if target lib is loaded
//    do {
//        sleep(1);
//    } while (!isLibraryLoaded(targetLibName));

    //Anti-lib rename
//    do {
//        sleep(1);
//    } while (!isLibraryLoaded("libYOURNAME.so"));

    //Anti-leech
//    if (!iconValid || !initValid || !settingsValid) {
//        //Bad function to make it crash
//        sleep(5);
//        int *p = 0;
//        *p = 0;
//    }

    return nullptr;
}

//==================================================================================================
__attribute__((constructor))
void lib_main() {
    // Create a new thread so it does not block the main thread, means the game would not freeze
    pthread_t ptid;
    pthread_create(&ptid, nullptr, hack_thread, nullptr);
}

//==================================================================================================
int RegisterFunctions(JNIEnv *env) {
    JNINativeMethod methods[] = {
            {OBFUSCATE("Start"),         OBFUSCATE(
                                                 "(Landroid/content/Context;)V"),                                      (void *) APKKill},
            {OBFUSCATE("processInvoke"), OBFUSCATE(
                                                 "(Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;"), (void *) processInvoke}
    };

    jclass clazz = env->FindClass(OBFUSCATE("com/muffin/APKKiller"));
    if (!clazz) {
        Debug_Log("Err 1");
        return JNI_ERR;
    }

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0) {
        Debug_Log("Err 2");
        return JNI_ERR;
    }

    JNINativeMethod methodsPrefs[] = {
            {OBFUSCATE("Changes"),
             OBFUSCATE("(Landroid/content/Context;ILjava/lang/String;IZLjava/lang/String;)V"),
             (void *) Changes},
    };
    jclass clazzPrefs = env->FindClass(OBFUSCATE("com/android/support/Preferences"));
    if (!clazzPrefs) {
        Debug_Log("Err 3");
        return JNI_ERR;
    }
    if (env->RegisterNatives(clazzPrefs, methodsPrefs,
                             sizeof(methodsPrefs) / sizeof(methodsPrefs[0])) != 0) {
        Debug_Log("Err 4");
        return JNI_ERR;
    }

    JNINativeMethod methodsMenu[] = {
            {OBFUSCATE("Icon"),            OBFUSCATE(
                                                   "()Ljava/lang/String;"),                                                           (void *) Icon},
            {OBFUSCATE("IconWebViewData"), OBFUSCATE(
                                                   "()Ljava/lang/String;"),                                                           (void *) IconWebViewData},
            {OBFUSCATE("IsGameLibLoaded"), OBFUSCATE(
                                                   "()Z"),                                                                            (void *) isGameLibLoaded},
            {OBFUSCATE("Init"),            OBFUSCATE(
                                                   "(Landroid/content/Context;Landroid/widget/TextView;Landroid/widget/TextView;)V"), (void *) Init},
            {OBFUSCATE("SettingsList"),    OBFUSCATE(
                                                   "()[Ljava/lang/String;"),                                                          (void *) SettingsList},
            {OBFUSCATE("GetFeatureList"),  OBFUSCATE(
                                                   "()[Ljava/lang/String;"),                                                          (void *) GetFeatureList},
    };

    jclass clazzMenu = env->FindClass(OBFUSCATE("com/android/support/Menu"));
    if (!clazzMenu) {
        Debug_Log("Err 5");
        return JNI_ERR;
    }
    if (env->RegisterNatives(clazzMenu, methodsMenu,
                             sizeof(methodsMenu) / sizeof(methodsMenu[0])) != 0) {
        Debug_Log("Err 6");
        return JNI_ERR;
    }

    return JNI_OK;
}
//==================================================================================================
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    g_vm = vm;

    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        Debug_Log("Error retrieving env!");
        return JNI_ERR;
    }

    if (RegisterFunctions(env) != 0) {
        Debug_Log("Error registering natives!");
        return JNI_ERR;
    }

    Debug_Log("JNI_OnLoad!");

    return JNI_VERSION_1_6;
}
//==================================================================================================