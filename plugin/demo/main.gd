extends Node2D

# TODO: Update to match your plugin's name
var _plugin_name = "GodotAndroidPluginTemplate"
var _android_plugin

func _ready():
	if Engine.has_singleton(_plugin_name):
		_android_plugin = Engine.get_singleton(_plugin_name)
		_android_plugin.connect("testSignal",testSignal)
	else:
		printerr("Couldn't find plugin " + _plugin_name)

func _on_Button_pressed():
	if _android_plugin:
		# TODO: Update to match your plugin's API
		_android_plugin.helloWorld()
		
func testSignal(data):
	_android_plugin.ShowToast("Godot message: "+str(data))

func _on_button_2_pressed() -> void:
	if _android_plugin:
		_android_plugin.InitIronSource(get_instance_id(), "85460dcd")


func _on_button_interstitial_pressed() -> void:
	if _android_plugin:
		_android_plugin.ShowInterstitial();

#Ok here we need a callback
#But I dont have idea 
func _on_button_reward_pressed() -> void:
	if _android_plugin:
		_android_plugin.ShowRewardVideo("on_rewarded_video_complete")
		
func on_rewarded_video_complete()-> void:
	if _android_plugin:
		_android_plugin.ShowToast("Reward Claimed")
		
func on_rewarded_video_ad_closed(name: String, amount: int):
	if _android_plugin:
		_android_plugin.ShowToast("Reward claimed")
