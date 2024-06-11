extends Node2D

# TODO: Update to match your plugin's name
#var _plugin_name = "GodotAndroidPluginTemplate"
#var _android_plugin

func _ready():
	pass
	#LevelPlayAddon._init()
	#if Engine.has_singleton(_plugin_name):
		#_android_plugin = Engine.get_singleton(_plugin_name)
		#_android_plugin.connect("testSignal",testSignal)
	#else:
		#printerr("Couldn't find plugin " + _plugin_name)

func _on_Button_pressed():
	LevelPlayAddon._showHelloWorld()
		
func testSignal(data):
	pass
	#_android_plugin.ShowToast("Godot message: "+str(data))

func _on_button_2_pressed() -> void:
	LevelPlayAddon._initializeLevelPlay()
	pass
	#if _android_plugin:
		#_android_plugin.InitIronSource(get_instance_id(), "85460dcd")


func _on_button_interstitial_pressed() -> void:
	LevelPlayAddon.ShowInterstitial()
	pass
	#if _android_plugin:
		#_android_plugin.ShowInterstitial();

#Ok here we need a callback
#But I dont have idea 
func _on_button_reward_pressed() -> void:
	LevelPlayAddon.ShowRewardVideo("on_rewarded_video_complete")
	pass
		
func on_rewarded_video_complete()-> void:
	pass
	#if _android_plugin:
		#_android_plugin.ShowToast("Reward Claimed")
		
func on_rewarded_video_ad_closed(name: String, amount: int):
	pass
	#if _android_plugin:
		#_android_plugin.ShowToast("Reward claimed")
