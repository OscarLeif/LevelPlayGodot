extends Node
class_name LevelPlayHelper

var _plugin_name = "GodotAndroidPluginTemplate"
var _plugin_singleton

var InterstitialAvailable: bool

var IsCoppaUser:bool
var IsGDPRUser:bool

var savedCoppaSetting:bool
var savedGDPRSetting:bool
var savedAge:int = -1 # if Age is loaded means we can Skip Age verification. But Also need to change later for future chanages

var save_path_GDPR = "user://User_GDPR.save"
var save_path_COPPA= "user://User_COPPA.save"
var save_path_AGE = "user://User_age.save"

func _ready() -> void:
	_init()

#Autoaload We Make ready the Singleton Node available at start
func _init() -> void:
	if(Engine.has_singleton(_plugin_name)):
		_plugin_singleton=Engine.get_singleton(_plugin_name)
		#_plugin_singleton.connect("CloseRewardGodot",testSignal)#The Reward Android to Godot is Here
		_plugin_singleton.connect("SetInterstitialAvailable", SetInterstitialAvailable)
		_plugin_singleton.connect("Interstitial_onAdClosed", OnInterstitialClosed)
		_plugin_singleton.connect("GetConsentSettings" , GetConsentSettings)
		_plugin_singleton.GatherConsentSettings()#Java Call
		load_saved_Age()
		print("Level Play Singleton Ready")
		
func save_Age():
	var file = FileAccess.open(save_path_AGE, FileAccess.WRITE)
	file.store_var(savedAge)	
	
func load_saved_Age():
	if FileAccess.file_exists(save_path_AGE):
		var file = FileAccess.open(save_path_AGE, FileAccess.READ)
		savedAge = file.get_var()
	else:
		savedAge=-1

func save_GDPR():
	var file = FileAccess.open(save_GDPR(), FileAccess.WRITE)
	file.store_var(savedGDPRSetting)

func load_GDPR():
	if FileAccess.file_exists(save_path_GDPR):
		#file found
		var file = FileAccess.open(save_path_GDPR, FileAccess.READ)
		savedGDPRSetting = file.get_var()
	else:
		#file not found
		savedGDPRSetting = false


#Java Callback
#Figure out if the User Is on USA or Europe
func GetConsentSettings( UseGDPR:bool, UseCOPPA:bool):
	if UseGDPR:
		IsGDPRUser = true
		pass
	if UseCOPPA:
		IsCoppaUser=true
		pass
	if UseGDPR==false and UseCOPPA==false:
		pass
	ShowToast("GATHER GDPR and COPPA User")

func _initializeLevelPlay():
	if _plugin_singleton:
		_plugin_singleton.InitIronSource(get_instance_id(), "85460dcd")

func _showHelloWorld():
		ShowToast("Hello World 1.0")

func ShowInterstitial():
	if _plugin_singleton:
		_plugin_singleton.ShowInterstitial()

func RefreshInterstitialReady():
	return _plugin_singleton.RefreshInterstitialReady()

func ShowRewardVideo(method:String="on_rewarded_video_complete"):
	if _plugin_singleton:
		_plugin_singleton.ShowRewardVideo("on_rewarded_video_complete")
		
func SetUserConsent(_userAge:int):
	if _plugin_singleton:
		_plugin_singleton.SubmitConsent(_userAge);
		

#Java Callback
func SetInterstitialAvailable(isAvailable: bool) -> void:
	InterstitialAvailable = isAvailable
	print("InterstitialAvailable set to: %s" % str(InterstitialAvailable))

#Java callback
func OnInterstitialClosed() ->void:
	ShowToast("Yes the interstitial was Closed")

func ShowToast(message: String):
	if _plugin_singleton:
		_plugin_singleton.ShowToast(message)

func getNumber()->int:
	if Engine.has_singleton(_plugin_name):
		var plugin = Engine.get_singleton(_plugin_name)
		var value = plugin.call("getIntegerValue");
		print("Returned value from android plugin: ", value)
		return value
	else:
		print("Android plugin not found")
		return -1

func getBoolean()->void:
	var getTrue = false
	var getFalse= true
	if _plugin_singleton:
		getTrue = _plugin_singleton.call("getTrue")
		print("Get True must be True: " + str(getTrue))
		getFalse = _plugin_singleton.call("getFalse")
		print("Get False must be False: " + str(getFalse))

