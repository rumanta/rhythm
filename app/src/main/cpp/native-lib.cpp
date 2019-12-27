#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_william_1rumanta_rhythm_NavigationMenuActivity_getExplanationPermission(
        JNIEnv *env,
        jobject /* this */) {
    std::string permission_explaination = "Dear beloved users,\n\n1. Phone state permission:\nDibutuhkan untuk melakukan pause ketika device sedang menerima call\n\n\
                                          2. Storage permission:\nDibutuhkan untuk mengakses file music pada local storage device\n\n\
                                          3. Record audio permission:\nDibutuhkan untuk menampilkan hasil visualizer dari musik yang dinyalakan";
    return env->NewStringUTF(permission_explaination.c_str());
}
