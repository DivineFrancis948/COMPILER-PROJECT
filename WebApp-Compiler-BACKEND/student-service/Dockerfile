FROM ubuntu:latest

# Install ClamAV
RUN apt-get update && \
    apt-get install -y clamav

# Run freshclam to update virus definitions
RUN freshclam

# Set the working directory
WORKDIR /app1

# Copy the executable to the container
COPY . /app1

# Specify the entry point
CMD ["clamscan", "--remove", "--recursive", "--infected", "--no-summary", "/app1"]
