@tool
extends EditorPlugin

const LEVELPLAY_AUTOLOAD := "LevelPlay"

# A class member to hold the editor export plugin during its lifecycle.
var export_plugin : AndroidExportPlugin

func _enter_tree():
	# Initialization of the plugin goes here.
	export_plugin = AndroidExportPlugin.new()
	add_export_plugin(export_plugin)


func _exit_tree():
	# Clean-up of the plugin goes here.
	remove_export_plugin(export_plugin)
	remove_autoload_singleton(LEVELPLAY_AUTOLOAD)
	export_plugin = null

func _add_autoloads() -> void:
	add_autoload_singleton(LEVELPLAY_AUTOLOAD, "res://addons/GodotAndroidPluginTemplate/LevelPlayAddon.gd")

func _remove_autoloads()->void:
	remove_autoload_singleton(LEVELPLAY_AUTOLOAD)

class AndroidExportPlugin extends EditorExportPlugin:
	# TODO: Update to your plugin's name.
	var _plugin_name = "GodotAndroidPluginTemplate"

	func _supports_platform(platform):
		if platform is EditorExportPlatformAndroid:
			return true
		return false

	func _get_android_libraries(platform, debug):
		if debug:
			return PackedStringArray([_plugin_name + "/bin/debug/" + _plugin_name + "-debug.aar"])
		else:
			return PackedStringArray([_plugin_name + "/bin/release/" + _plugin_name + "-release.aar"])

	func _get_android_dependencies(platform, debug):
		# TODO: Add remote dependices here.
		if not _supports_platform(platform):
			return PackedStringArray()

		return PackedStringArray([
			"com.ironsource.sdk:mediationsdk:8.1.0",
			"com.ironsource:adqualitysdk:7.20.0",
			"com.google.android.gms:play-services-appset:16.0.0",
			"com.google.android.gms:play-services-ads-identifier:18.0.1",
			"com.google.android.gms:play-services-basement:18.1.0",
			"com.android.support:appcompat-v7:26.1.0",
			"com.android.support:support-v4:26.1.0",
			"com.ironsource.adapters:facebookadapter:4.3.46",
			"com.facebook.android:audience-network-sdk:6.17.0",
			"com.ironsource.adapters:admobadapter:4.3.43",
			"com.ironsource.adapters:admobadapter:4.3.43"
			])

	func _get_android_dependencies_maven_repos(platform: EditorExportPlatform, debug: bool) -> PackedStringArray:
		return PackedStringArray([
			"https://android-sdk.is.com/"
		])

	func _get_android_manifest_application_element_contents(platform: EditorExportPlatform, debug: bool) -> String:
		if not _supports_platform(platform):
			return ""

		return "<meta-data android:name=\"com.google.android.gms.ads.APPLICATION_ID\" android:value=\"ADMOB_APP_ID\"/>"

	func _get_name():
		return _plugin_name
