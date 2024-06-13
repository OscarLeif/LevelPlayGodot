extends Node2D

# TODO: Update to match your plugin's name
#var _plugin_name = "GodotAndroidPluginTemplate"
#var _android_plugin

var UserAge:int = 16

@onready var DisplayText:TextEdit = get_node("CanvasLayer/PanelStepper/TextEdit")

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


func _on_button_interstitial_available_pressed():
	#we could trigger a method to update the signal
	if LevelPlayAddon.InterstitialAvailable:
		LevelPlayAddon.ShowToast("Interstitial Ready")
	else:
		LevelPlayAddon.ShowToast("Interstitial Not Ready")
	pass # Replace with function body.




func _on_button_positive_pressed() -> void:
	UserAge = UserAge+1
	if UserAge >100:
		UserAge=100
	DisplayText.text = str(UserAge)
	pass # Replace with function body.


func _on_button_negative_pressed() -> void:
	UserAge = UserAge-1
	if(UserAge<=7):
		UserAge=7
	DisplayText.text = str(UserAge)
	pass # Replace with function body.


func _on_button_submit_pressed() -> void:
	LevelPlayAddon.SetConsent(UserAge)
	pass # Replace with function body.
