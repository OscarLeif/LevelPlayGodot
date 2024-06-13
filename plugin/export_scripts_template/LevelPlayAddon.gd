extends Node
class_name LevelPlayHelper

var _plugin_name = "GodotAndroidPluginTemplate"
var _plugin_singleton

var InterstitialAvailable: bool

func _ready() -> void:
	_init()

#Autoaload We Make ready the Singleton Node available at start
func _init() -> void:
	if(Engine.has_singleton(_plugin_name)):
		_plugin_singleton=Engine.get_singleton(_plugin_name)
		#_plugin_singleton.connect("testSignal",testSignal)#The Reward Android to Godot is Here
		#_plugin_singleton.connect("CloseRewardGodot",testSignal)#The Reward Android to Godot is Here
		_plugin_singleton.connect("SetInterstitialAvailable", SetInterstitialAvailable)
		print("Level Play Singleton Ready")

func _initializeLevelPlay():
	if _plugin_singleton:
		_plugin_singleton.InitIronSource(get_instance_id(), "85460dcd")

func _showHelloWorld():
	if _plugin_singleton:
		# TODO: Update to match your plugin's API
		_plugin_singleton.ShowToast("Hello Plugin is improved")

func ShowInterstitial():
	if _plugin_singleton:
		_plugin_singleton.ShowInterstitial()

func RefreshInterstitialReady():
	return _plugin_singleton.RefreshInterstitialReady()

func ShowRewardVideo(method:String="on_rewarded_video_complete"):
	if _plugin_singleton:
		_plugin_singleton.ShowRewardVideo("on_rewarded_video_complete")

#Java Callback
func testSignal(data):
	pass
	_plugin_singleton.ShowToast("Godot message: "+str(data))

#Java Callback
func SetInterstitialAvailable(isAvailable):
	InterstitialAvailable=isAvailable

func ShowToast(message: String):
	if _plugin_singleton:
		_plugin_singleton.ShowToast(message)
