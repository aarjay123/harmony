package com.nugget.hios.updates;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

public class GithubUpdater {

    private GithubUpdater() {}

    //Keep these stable
    private static final String APK_MIME = "application/vnd.android.package-archive";

    public static void checkAndUpdate(Activity activity, String owner, String repo) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                UpdateInfo info = fetchLatestRelease(owner, repo);
                if (info == null || info.apkUrl == null) return;

                String current = getCurrentVersionName(activity);
                boolean updateAvailable = isNewerSemver(info.versionName, current);

                activity.runOnUiThread(() -> {
                    if (!updateAvailable) {
                        new AlertDialog.Builder(activity)
                                .setTitle("Up to date")
                                .setMessage("You already have the latest version of HiClub installed.")
                                .setPositiveButton("Ok cool", null)
                                .show();
                        return;
                    }

                    new AlertDialog.Builder(activity)
                            .setTitle("Update available")
                            .setMessage("New version: " + info.versionName + "\n\nInstall now?")
                            .setNegativeButton("Not now please", null)
                            .setPositiveButton("Install", (d, w) -> downloadAndInstall(activity, info.apkUrl))
                            .show();
                });
            } catch (Exception e) {
                activity.runOnUiThread(() -> new AlertDialog.Builder(activity)
                        .setTitle("Update check failed \uD83D\uDE2D")
                        .setMessage(e.getMessage())
                        .setPositiveButton("Alright :(", null)
                        .show()
                );
            }
        });
    }

    private static void downloadAndInstall(Activity activity, String apkUrl) {
        //Android 8+ needs per-app permission to install apks, so a check is necessary.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!activity.getPackageManager().canRequestPackageInstalls()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
                return;
            }
        }

        DownloadManager dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(apkUrl);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        req.setTitle("Downloading update");
        req.setDestinationInExternalFilesDir(activity, Environment.DIRECTORY_DOWNLOADS, "update.apk");

        long downloadId = dm.enqueue(req);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                if (!DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) return;
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != downloadId) return;

                try {
                    activity.unregisterReceiver(this);
                } catch (Exception ignored) {}

                installDownloadedApk(activity, dm, downloadId);
            }
        };

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        if (Build.VERSION.SDK_INT >= 33) {
            activity.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            activity.registerReceiver(receiver, filter);
        }
    }

    private static void installDownloadedApk(Activity activity, DownloadManager dm, long downloadId) {
        Uri localUri = getDownloadedFileUri(dm, downloadId);
        if (localUri == null) return;

        File file = new File(localUri.getPath());
        Uri contentUri = FileProvider.getUriForFile(
                activity,
                activity.getPackageName() + ".fileprovider",
                file
        );

        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(contentUri, APK_MIME);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(install);
    }

    private static Uri getDownloadedFileUri(DownloadManager dm, long downloadId) {
        DownloadManager.Query q = new DownloadManager.Query().setFilterById(downloadId);
        try (Cursor c = dm.query(q)) {
            if (c == null || !c.moveToFirst()) return null;

            int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            if (status != DownloadManager.STATUS_SUCCESSFUL) return null;

            String local = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
            if (local == null) return null;

            return Uri.parse(local);
        }
    }

    private static String getCurrentVersionName(Context context) throws Exception {
        PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pi.versionName != null ? pi.versionName : "0.0.0";
    }

    private static UpdateInfo fetchLatestRelease(String owner, String repo) throws Exception {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/releases/latest");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/vnd.github+json");
        connection.setRequestProperty("User-Agent", "HiOSMobile-Updater");

        try (InputStream in = connection.getInputStream()) {
            byte[] bytes = readAll(in);
            String json = new String(bytes);

            JSONObject root = new JSONObject(json);
            String tag = root.optString("tag_name", "");
            String versionName = tag.startsWith("v") ? tag.substring(1) : tag;

            JSONArray assets = root.optJSONArray("assets");
            String apkURL = null;
            if (assets != null) {
                for (int i = 0; i < assets.length(); i++) {
                    JSONObject a = assets.getJSONObject(i);
                    String name = a.optString("name", "");
                    if (name.endsWith(".apk")) {
                        apkURL = a.optString("browser_download_url", null);
                        break;
                    }
                }
            }

            UpdateInfo info = new UpdateInfo();
            info.versionName = versionName;
            info.apkUrl = apkURL;
            return info;
        } finally {
            connection.disconnect();
        }
    }

    private static byte[] readAll(InputStream in) throws Exception {
        byte[] buffer = new byte[8192];
        int n;
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        while ((n = in.read(buffer)) > 0) out.write(buffer, 0, n);
        return out.toByteArray();
    }

    private static boolean isNewerSemver(String latest, String current) {
        int[] a = parseSemver(latest);
        int[] b = parseSemver(current);
        for (int i = 0; i < 3; i++) {
            if (a[i] != b[i]) return a[i] > b[i];
        }
        return false;
    }

    private static int[] parseSemver(String v) {
        int[] out = new int[]{0,0,0};
        if (v == null) return out;
        String[] parts = v.trim().split("\\.");
        for (int i = 0; i < Math.min(3, parts.length); i++) {
            try {
                out[i] = Integer.parseInt(parts[i].replaceAll("[^0-9]", ""));
            } catch (Exception ignored) {}
        }
        return out;
    }

    private static final class UpdateInfo {
        String versionName;
        String apkUrl;
    }
}
