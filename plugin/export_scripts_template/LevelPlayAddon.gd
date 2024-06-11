class_name LevelPlayHelper extends Node

var _plugin_name = "GodotAndroidPluginTemplate"
var _plugin_singleton

func _ready() -> void:
	_init()

#Autoaload We Make ready the Singleton Node available at start
func _init() -> void:
	if(Engine.has_singleton(_plugin_name)):
		_plugin_singleton=Engine.get_singleton(_plugin_name)
		_plugin_singleton.connect("testSignal",testSignal)#The Reward Android to Godot is Here
		print("Level Play Singleton Ready")
		
func _initializeLevelPlay():
	_plugin_singleton.InitIronSource(get_instance_id(), "85460dcd")
	
func _showHelloWorld():
	if _plugin_singleton:
		# TODO: Update to match your plugin's API
		_plugin_singleton.ShowToast("Hello Plugin is improved")

func ShowInterstitial():
	_plugin_singleton.ShowInterstitial()

func ShowRewardVideo(method:String="on_rewarded_video_complete"):
	_plugin_singleton.ShowRewardVideo("on_rewarded_video_complete")

Here is what the 
func testSignal(data):
	pass
	_plugin_singleton.ShowToast("Godot message: "+str(data))
