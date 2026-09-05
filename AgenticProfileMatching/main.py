from matching_agent import HiringAgent


if __name__ == "__main__":
    with open("job_description.txt", encoding="utf-8") as file:
        job_description = file.read()

    result = HiringAgent().run_agent(job_description)
    print(result["report"])
