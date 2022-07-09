import os

class ExperimentIndirectMeasurements:

    @staticmethod
    def writeInterArrivalTimes(measurementsFileName, resultsPath, timestampName):
        print("Starting to write inter arrival/batch times...")
        print("results will be stored here: " + resultsPath)
        #get lines from storage Handler
        strgapilines = ""
        f = open(measurementsFileName, 'r')
        try:
            strgapilines = f.readlines()
        finally:
            f.close()
        #remove lines that are not entry timestamps
        arrival_time_lines = [x for x in strgapilines if x.split(",")[1]==timestampName]
        #write inter arrival times
        fw = open(resultsPath,"w+")
        fw.write("number,time\n")
        prev_time = 0
        number = 0
        for line in arrival_time_lines:
            if prev_time == 0:
                prev_time = int(line.split(",")[2])
            else:
                curr_time = int(line.split(",")[2])
                if curr_time != prev_time:
                    inter_time = (curr_time - prev_time)/1000000
                    fw.write(str(number) + "," + str(inter_time) + "\n")
                    number += 1
                prev_time = curr_time
        print("finished writing inter arrival/batch times")


    @staticmethod
    def writeAugmentedDataset(measurementspath, augmentedDatasetFolderPath):
        #write residence times, end to end times, and service times
        augmentedDatasetPath = augmentedDatasetFolderPath + os.path.sep + "augmented.csv"
        apiPath = measurementspath + os.path.sep + "strgapi.txt"
        managerPath = measurementspath + os.path.sep + "strgMngr.txt"

        #get lines from storage Handler
        strgapilines = ""
        fapi = open(apiPath, 'r')
        try:
            strgapilines = fapi.readlines()
        finally:
            fapi.close()

        #get lines form storage manager
        strgMngrlines = ""
        fmngr = open(managerPath, 'r')
        try:
            strgMngrlines = fmngr.readlines()
        finally:
            fmngr.close()

        #pop first line because it is not measurements
        strgapilines.pop(0)
        strgMngrlines.pop(0)

        #get api residence times
        api_arrivals = [x for x in strgapilines if x.split(",")[1]=="STORAGE_API_ENTRY"]
        api_exits = [x for x in strgapilines if x.split(",")[1]=="TRANSMITTER_EXIT"]
        num_complete_api_req = len(api_exits)
        api_res_times = []
        for i in range(num_complete_api_req):
            api_res_time = (int(api_exits[i].split(",")[2]) - int(api_arrivals[i].split(",")[2]))/1000000
            api_res_times.append(api_res_time)

        #get manager residence times and service times
        mngr_arrivals = [x for x in strgMngrlines if x.split(",")[1]=="ENTRY"]
        mngr_q_exits = [x for x in strgMngrlines if x.split(",")[1]=="QueueExitTime"]
        mngr_exits = [x for x in strgMngrlines if x.split(",")[1]=="EXIT"]
        num_completed_mngr_req = len(mngr_exits)
        mngr_res_times = []
        mngr_st_times = []
        for i in range(num_completed_mngr_req):
            mngr_res_time = (int(mngr_exits[i].split(",")[2]) - int(mngr_arrivals[i].split(",")[2]))/1000000
            mngr_res_times.append(mngr_res_time)
            mngr_st_time = (int(mngr_exits[i].split(",")[2]) - int(mngr_q_exits[i].split(",")[2]))/1000000
            mngr_st_times.append(mngr_st_time)

        #write to augmented dataset file
        faug = open(augmentedDatasetPath, "w+")
        faug.write("num,api_res,mngr_res,total,st\n") #write headers
        for i in range(num_completed_mngr_req):
            faug.write(str(i)+","+str(api_res_times[i])+","+str(mngr_res_times[i])+","+str(api_res_times[i]+mngr_res_times[i])+","+str(mngr_st_times[i])+"\n")



#RUN script

#api inter arrival
path_before_measurements = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-07-09-test0\ca-rt-300000-ia-50-cy-150000-cl-1000-st-40"
measurementsFileNameApi = path_before_measurements + r"\measurements\strgapi.txt"
inter_arrival_times_resultsFileNameAPI = path_before_measurements + r"\inter_arrival_times.csv"
ExperimentIndirectMeasurements.writeInterArrivalTimes(measurementsFileNameApi,inter_arrival_times_resultsFileNameAPI,'STORAGE_API_ENTRY')

#manager inter batch times
measurementsFileNameMngr = path_before_measurements + r"\measurements\strgMngr.txt"
inter_arrival_times_resultsFileNameMngr = path_before_measurements + r"\batch_arrival_times.csv"
ExperimentIndirectMeasurements.writeInterArrivalTimes(measurementsFileNameMngr,inter_arrival_times_resultsFileNameMngr,'ENTRY')

#augmented dataset
exp_measurments_path = path_before_measurements + r"\measurements"
augmentedDatasetFolderPath = path_before_measurements
ExperimentIndirectMeasurements.writeAugmentedDataset(exp_measurments_path,augmentedDatasetFolderPath)



