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
        #write inter arrival times to Storage Handler, inter transfer times, residence times, end to end times, and service times
        augmentedDatasetPath = augmentedDatasetFolderPath + os.path.sep + "augmented.csv"
        apiPath = measurementspath + os.path.sep + "strgapi.txt"
        managerPath = measurementspath + os.path.sep + "strgMngr.txt"
        #f = open(augmentedDatasetPath,"w+")

        #get lines from storage Handler
        strgapilines = ""
        f = open(apiPath, 'r')
        try:
            strgapilines = f.readlines()
        finally:
            f.close()

        #get lines form storage manager
        strgMngrlines = ""
        f = open(managerPath, 'r')
        try:
            strgMngrlines = f.readlines()
        finally:
            f.close()

        #get inter arrival times
        #interarrival_times =




#RUN script
measurementsFileName = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-06-22-adaptive-test-2\ca-rt-300000-ia-50-cy-5000-cl-1000-st-40\measurements\strgapi.txt"
inter_arrival_times_resultsFileName = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-06-22-adaptive-test-2\ca-rt-300000-ia-50-cy-5000-cl-1000-st-40\inter_arrival_times.csv"
ExperimentIndirectMeasurements.writeInterArrivalTimes(measurementsFileName,inter_arrival_times_resultsFileName,'STORAGE_API_ENTRY')


measurementsFileNameMngr = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-06-22-adaptive-test-2\ca-rt-300000-ia-50-cy-5000-cl-1000-st-40\measurements\strgMngr.txt"
inter_arrival_times_resultsFileNameMngr = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-06-22-adaptive-test-2\ca-rt-300000-ia-50-cy-5000-cl-1000-st-40\batch_arrival_times.csv"
ExperimentIndirectMeasurements.writeInterArrivalTimes(measurementsFileNameMngr,inter_arrival_times_resultsFileNameMngr,'ENTRY')

